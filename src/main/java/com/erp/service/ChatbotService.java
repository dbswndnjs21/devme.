package com.erp.service;

import com.erp.dto.ChatResponse;
import com.erp.dto.StudyDto;
import com.erp.entity.Study;
import com.erp.repository.StudyRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatbotService {

    private final StudyRepository studyRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${gemini.api.key:}")
    private String apiKey;

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent";
    private static final double EARTH_RADIUS_KM = 6371.0;

    public ChatResponse processMessage(String userMessage, Double latitude, Double longitude,
                                       CustomUserDetails userDetails) {

        // 1. 메시지 분석하여 검색 범위 결정
        List<StudyDto> studies = new ArrayList<>();
        String lowerMessage = userMessage.toLowerCase();

        // "근처", "가까운", "주변" 등의 키워드가 있으면 위치 기반 검색
        boolean isNearbySearch = lowerMessage.contains("근처") ||
                lowerMessage.contains("가까운") ||
                lowerMessage.contains("주변") ||
                lowerMessage.contains("내 위치");

        if (isNearbySearch && latitude != null && longitude != null) {
            // 위치 기반 검색 (10km 반경)
            studies = findNearbyStudies(latitude, longitude, 10.0);
        } else {
            // 전체 스터디 검색
            studies = findAllStudies(latitude, longitude);
        }

        // 2. AI에게 전달할 컨텍스트 생성
        String context = buildContext(studies, userDetails, isNearbySearch);

        // 3. Gemini API 호출
        String aiResponse = callGeminiAPI(userMessage, context);

        // 4. 응답 반환
        return ChatResponse.builder()
                .message(aiResponse)
                .recommendedStudies(studies.isEmpty() ? null : studies.subList(0, Math.min(3, studies.size())))
                .build();
    }

    private List<StudyDto> findNearbyStudies(double userLat, double userLon, double radiusKm) {
        List<Study> allStudies = studyRepository.findAll();

        return allStudies.stream()
                .filter(study -> study.getLocationLatitude() != null && study.getLocationLongitude() != null)
                .map(study -> {
                    double distance = calculateDistance(userLat, userLon,
                            study.getLocationLatitude(), study.getLocationLongitude());
                    return new StudyWithDistance(study, distance);
                })
                .filter(sd -> sd.distance <= radiusKm)
                .sorted(Comparator.comparingDouble(sd -> sd.distance))
                .limit(10)
                .map(sd -> convertToDto(sd.study, sd.distance))
                .collect(Collectors.toList());
    }

    private List<StudyDto> findAllStudies(Double userLat, Double userLon) {
        List<Study> allStudies = studyRepository.findAll();

        // 위치 정보가 있으면 거리 계산, 없으면 0으로 설정
        return allStudies.stream()
                .map(study -> {
                    double distance = 0.0;
                    if (userLat != null && userLon != null &&
                            study.getLocationLatitude() != null &&
                            study.getLocationLongitude() != null) {
                        distance = calculateDistance(userLat, userLon,
                                study.getLocationLatitude(), study.getLocationLongitude());
                    }
                    return convertToDto(study, distance);
                })
                .limit(20) // 전체 검색은 최대 20개까지
                .collect(Collectors.toList());
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    private String buildContext(List<StudyDto> studies, CustomUserDetails userDetails, boolean isNearbySearch) {
        StringBuilder context = new StringBuilder();

        if (userDetails != null) {
            context.append("사용자 정보: ").append(userDetails.getUsername()).append("\n\n");
        }

        if (!studies.isEmpty()) {
            if (isNearbySearch) {
                context.append("근처 스터디 목록:\n");
            } else {
                context.append("전체 스터디 목록:\n");
            }

            for (int i = 0; i < studies.size(); i++) {
                StudyDto study = studies.get(i);
                if (isNearbySearch && study.getDistance() != null && study.getDistance() > 0) {
                    context.append(String.format("%d. %s - %s (약 %.1fkm 거리)\n",
                            i + 1,
                            study.getTitle(),
                            study.getDescription() != null ? study.getDescription() : "설명 없음",
                            study.getDistance()));
                } else {
                    context.append(String.format("%d. %s - %s\n",
                            i + 1,
                            study.getTitle(),
                            study.getDescription() != null ? study.getDescription() : "설명 없음"));
                }
            }
        } else {
            if (isNearbySearch) {
                context.append("현재 근처에 스터디가 없습니다.\n");
            } else {
                context.append("현재 등록된 스터디가 없습니다.\n");
            }
        }

        return context.toString();
    }

    private String callGeminiAPI(String userMessage, String context) {
        // API 키가 설정되지 않은 경우 기본 응답 반환
        if (apiKey == null || apiKey.isEmpty()) {
            return generateFallbackResponse(userMessage, context);
        }

        try {
            String systemInstruction = "당신은 스터디 매칭 플랫폼의 AI 어시스턴트입니다. " +
                    "사용자의 질문에 친절하게 답변하고, 적절한 스터디를 추천해주세요. " +
                    "답변은 간결하고 명확하게 한국어로 작성하세요.";

            String fullPrompt = systemInstruction + "\n\n컨텍스트:\n" + context +
                    "\n\n사용자 질문: " + userMessage;

            // Gemini API 요청 구조
            Map<String, Object> requestBody = new HashMap<>();

            Map<String, Object> part = new HashMap<>();
            part.put("text", fullPrompt);

            Map<String, Object> content = new HashMap<>();
            content.put("parts", Arrays.asList(part));

            requestBody.put("contents", Arrays.asList(content));

            // API 키를 URL 파라미터로 추가
            String urlWithKey = GEMINI_API_URL + "?key=" + apiKey;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    urlWithKey,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            // Gemini 응답 파싱
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            return jsonResponse.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();

        } catch (HttpClientErrorException e) {
            // API 키 오류 또는 할당량 초과
            if (e.getStatusCode().value() == 400) {
                log.warn("Gemini API 요청 오류. 기본 응답으로 전환합니다.");
                return generateFallbackResponse(userMessage, context);
            } else if (e.getStatusCode().value() == 429) {
                log.warn("Gemini API 할당량 초과 (Rate Limit). 기본 응답으로 전환합니다.");
                return generateFallbackResponse(userMessage, context);
            } else if (e.getStatusCode().value() == 403) {
                log.warn("Gemini API 권한 오류. 기본 응답으로 전환합니다.");
                return generateFallbackResponse(userMessage, context);
            } else {
                log.error("Gemini API 에러: " + e.getStatusCode(), e);
                return generateFallbackResponse(userMessage, context);
            }
        } catch (Exception e) {
            log.error("Gemini API 호출 실패", e);
            return generateFallbackResponse(userMessage, context);
        }
    }

    private String generateFallbackResponse(String userMessage, String context) {
        // AI API 사용 불가 시 기본 응답
        String lowerMessage = userMessage.toLowerCase();

        if (lowerMessage.contains("추천") || lowerMessage.contains("스터디") ||
                lowerMessage.contains("찾아") || lowerMessage.contains("근처")) {

            if (context.contains("스터디 목록")) {
                return "스터디를 찾았습니다! 위의 목록을 확인해보세요. " +
                        "각 스터디의 설명을 참고하여 자신에게 맞는 스터디를 선택하실 수 있습니다.";
            } else {
                return "죄송합니다. 현재 조건에 맞는 스터디가 없습니다. " +
                        "검색 범위를 넓히시거나, 직접 스터디를 만들어보시는 건 어떨까요?";
            }
        }

        return "무엇을 도와드릴까요? 다음과 같이 질문해보세요:\n" +
                "- '내 위치 근처 스터디 추천해줘' (위치 기반 검색)\n" +
                "- 'Java 스터디 찾아줘' (전체 검색)\n" +
                "- '온라인 스터디 추천해줘' (전체 검색)";
    }

    private StudyDto convertToDto(Study study, double distance) {
        return StudyDto.builder()
                .id(study.getId())
                .title(study.getTitle())
                .description(study.getDescription())
                .maxMembers(study.getMaxMembers())
                .locationAddress(study.getLocationAddress())
                .locationLatitude(study.getLocationLatitude())
                .locationLongitude(study.getLocationLongitude())
                .distance(distance)
                .createdById(study.getCreatedBy() != null ? study.getCreatedBy().getId() : null)
                .createdByUsername(study.getCreatedBy() != null ? study.getCreatedBy().getUsername() : null)
                .createdAt(study.getCreatedAt())
                .status(study.getStatus() != null ? study.getStatus().name() : null)
                .build();
    }

    // Helper class
    private static class StudyWithDistance {
        Study study;
        double distance;

        StudyWithDistance(Study study, double distance) {
            this.study = study;
            this.distance = distance;
        }
    }
}