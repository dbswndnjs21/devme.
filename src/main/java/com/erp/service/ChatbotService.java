package com.erp.service;

import com.erp.dto.ChatResponse;
import com.erp.dto.StudyDto;
import com.erp.entity.Study;
import com.erp.enums.StudyType;
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

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";
    private static final double EARTH_RADIUS_KM = 6371.0;

    private final StudyRepository studyRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${gemini.api.key}")
    private String apiKey;

    /**
     * 사용자의 메시지를 처리하여 AI 답변과 추천 스터디 리스트를 반환합니다.
     */
    public ChatResponse processMessage(String userMessage, Double latitude, Double longitude,
                                       CustomUserDetails userDetails) {

        // 1. 모든 데이터를 가져오고 거리순으로 정렬 (최대 15개로 제한하여 네트워크 오류 방지)
        List<StudyDto> allStudies = findAllStudies(latitude, longitude);
        List<StudyDto> matchedStudies = filterStudiesByQuery(allStudies, userMessage);
        List<StudyDto> candidateStudies = matchedStudies.isEmpty() && !allStudies.isEmpty()
                ? (extractKeywords(userMessage).isEmpty() ? allStudies : Collections.emptyList())
                : matchedStudies;
        List<StudyDto> contextStudies = limitForContext(candidateStudies);

        // 2. AI에게 전달할 컨텍스트 생성
        String context = buildContext(contextStudies, userDetails);

        // 3. AI의 역할과 행동 지침 정의 (프롬프트 엔지니어링)
        String systemInstruction = "당신은 스터디 매칭 플랫폼의 전문 매니저입니다.\n" +
                "1. 제공된 '컨텍스트' 내의 실제 데이터를 바탕으로 답변하세요.\n" +
                "2. 사용자가 위치 관련(근처, 가까운 등) 질문을 하면 '거리'가 짧은 스터디를 우선 추천하세요.\n" +
                "3. 주제 관련(스포츠, 공부 등) 질문을 하면 제목과 설명이 일치하는 것을 우선 추천하세요.\n" +
                "4. 반드시 '추천 후보' 목록 안에 있는 스터디만 언급하고, 목록이 비어 있으면 없다고 안내하세요.\n" +
                "5. 목록에 있는 스터디에 대해 '상세 정보가 없다'거나 '업데이트 예정'이라는 말 대신, 제목을 기반으로 긍정적으로 안내하세요.\n" +
                "6. 친절하고 열정적인 한국어로 답변하세요.";

        // 4. Gemini API 호출 (작성한 지침과 컨텍스트 전달)
        String aiResponse = callGeminiAPI(userMessage, context, systemInstruction);

        // 5. 하단 카드 리스트는 상위 3개만 노출
        return ChatResponse.builder()
                .message(aiResponse)
                .recommendedStudies(contextStudies.isEmpty() ? null :
                        contextStudies.subList(0, Math.min(3, contextStudies.size())))
                .build();
    }

    private List<StudyDto> findAllStudies(Double userLat, Double userLon) {
        List<Study> allStudies = studyRepository.findAll();

        return allStudies.stream()
                .map(study -> {
                    Double distance = null;
                    // 오프라인 스터디이고 위치 정보가 있는 경우에만 거리 계산
                    if (study.getStudyType() == StudyType.OFFLINE &&
                            userLat != null && userLon != null &&
                            study.getLocationLatitude() != null &&
                            study.getLocationLongitude() != null) {
                        distance = calculateDistance(userLat, userLon,
                                study.getLocationLatitude(), study.getLocationLongitude());
                    }
                    return convertToDto(study, distance);
                })
                // 거리순 정렬 (거리가 가까운 순, 온라인/거리없음은 뒤로)
                .sorted(Comparator.comparing(StudyDto::getDistance,
                        Comparator.nullsLast(Comparator.naturalOrder())))
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

    private String buildContext(List<StudyDto> studies, CustomUserDetails userDetails) {
        StringBuilder context = new StringBuilder();
        if (userDetails != null) {
            context.append("사용자 이름: ").append(userDetails.getUsername()).append("\n\n");
        }

        if (!studies.isEmpty()) {
            context.append("현재 운영 중인 스터디 목록:\n");
            for (int i = 0; i < studies.size(); i++) {
                StudyDto study = studies.get(i);
                context.append(String.format("- %s: %s", study.getTitle(),
                        study.getDescription() != null ? study.getDescription() : "함께 성장할 멤버를 모집 중입니다."));
                if (study.getDistance() != null) {
                    context.append(String.format(" (거리: %.1fkm)", study.getDistance()));
                }
                context.append("\n");
            }
            context.append("\n추천 후보(최대 3개, 반드시 이 중에서만 추천):\n");
            for (int i = 0; i < Math.min(3, studies.size()); i++) {
                StudyDto study = studies.get(i);
                context.append(String.format("- %s", study.getTitle()));
                if (study.getDistance() != null) {
                    context.append(String.format(" (거리: %.1fkm)", study.getDistance()));
                }
                context.append("\n");
            }
        } else {
            context.append("현재 등록된 스터디 정보가 없습니다.\n");
            context.append("추천 후보가 없습니다.\n");
        }
        return context.toString();
    }

    private String callGeminiAPI(String userMessage, String context, String systemInstruction) {
        if (apiKey == null || apiKey.isEmpty()) {
            log.error("Gemini API Key가 설정되지 않았습니다.");
            return "시스템 설정 오류가 발생했습니다. 관리자에게 문의하세요.";
        }

        try {
            // 외부에서 정의한 systemInstruction과 context, userMessage를 하나로 합침
            String fullPrompt = systemInstruction + "\n\n[컨텍스트]\n" + context +
                    "\n\n[사용자 질문]\n" + userMessage;

            Map<String, Object> requestBody = new HashMap<>();
            List<Map<String, Object>> contents = new ArrayList<>();
            Map<String, Object> content = new HashMap<>();
            List<Map<String, Object>> parts = new ArrayList<>();
            Map<String, Object> part = new HashMap<>();

            part.put("text", fullPrompt);
            parts.add(part);
            content.put("parts", parts);
            contents.add(content);
            requestBody.put("contents", contents);

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

            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();

        } catch (HttpClientErrorException e) {
            log.error("API 호출 중 오류 발생: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return "현재 요청이 너무 많습니다. 잠시 후 다시 시도해 주세요.";
        } catch (Exception e) {
            log.error("알 수 없는 에러 발생", e);
            return "응답 생성 중 오류가 발생했습니다.";
        }
    }

    private StudyDto convertToDto(Study study, Double distance) {
        return StudyDto.builder()
                .id(study.getId())
                .title(study.getTitle())
                .description(study.getDescription())
                .studyType(study.getStudyType())
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

    private List<StudyDto> limitForContext(List<StudyDto> studies) {
        if (studies == null || studies.isEmpty()) {
            return Collections.emptyList();
        }
        return studies.stream()
                .limit(15)
                .collect(Collectors.toList());
    }

    private List<StudyDto> filterStudiesByQuery(List<StudyDto> studies, String userMessage) {
        List<String> keywords = extractKeywords(userMessage);
        if (keywords.isEmpty()) {
            return studies;
        }
        return studies.stream()
                .filter(study -> matchesKeywords(study, keywords))
                .collect(Collectors.toList());
    }

    private boolean matchesKeywords(StudyDto study, List<String> keywords) {
        String title = study.getTitle() != null ? study.getTitle().toLowerCase(Locale.ROOT) : "";
        String description = study.getDescription() != null ? study.getDescription().toLowerCase(Locale.ROOT) : "";
        for (String keyword : keywords) {
            if (title.contains(keyword) || description.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private List<String> extractKeywords(String userMessage) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return Collections.emptyList();
        }
        Set<String> stopwords = new HashSet<>(Arrays.asList(
                "스터디", "공부", "찾아줘", "추천", "근처", "가까운", "어디", "어떤", "있어", "있나요", "해줘",
                "요", "좀", "관련", "원해", "원합니다", "주세요", "가능", "할", "수", "있는", "모집",
                "하고", "하는", "모임", "찾기", "검색", "알려줘", "알려", "추천해줘", "추천해", "싶어"
        ));

        String[] tokens = userMessage.toLowerCase(Locale.ROOT).split("[^\\p{L}\\p{N}]+");
        List<String> keywords = new ArrayList<>();
        for (String token : tokens) {
            if (token.length() < 2) {
                continue;
            }
            if (stopwords.contains(token)) {
                continue;
            }
            keywords.add(token);
        }
        return keywords;
    }
}
