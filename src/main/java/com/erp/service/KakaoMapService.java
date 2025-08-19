package com.erp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoMapService {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Double> getCoordinates(String address) {
        try {
            log.info("주소 검색 요청: {}", address);
            log.info("API 키 존재 여부: {}", kakaoApiKey != null && !kakaoApiKey.isEmpty());

            String url = "https://dapi.kakao.com/v2/local/search/address.json";

            // 한글 주소 안전하게 인코딩
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);

            URI uri = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("query", encodedAddress)
                    .build(true) // build(true)로 이미 인코딩된 문자열 그대로 사용(이중 인코딩문제로 추가)
                    .toUri();

            log.info("요청 URL: {}", uri);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + kakaoApiKey);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            log.info("응답 상태 코드: {}", response.getStatusCode());
            Map<String, Object> body = response.getBody();
            log.info("전체 응답: {}", body);

            if (body == null) return null;

            List<Map<String, Object>> documents = (List<Map<String, Object>>) body.get("documents");
            if (documents == null || documents.isEmpty()) {
                log.warn("검색 결과가 없습니다.");
                return null;
            }

            Map<String, Object> firstResult = documents.get(0);
            Map<String, Object> addressInfo = firstResult.containsKey("road_address") ?
                    (Map<String, Object>) firstResult.get("road_address") :
                    (Map<String, Object>) firstResult.get("address");

            if (addressInfo == null) return null;

            String xStr = (String) addressInfo.get("x");
            String yStr = (String) addressInfo.get("y");

            if (xStr == null || yStr == null) return null;

            Double longitude = Double.parseDouble(xStr);
            Double latitude = Double.parseDouble(yStr);

            Map<String, Double> coordinates = new HashMap<>();
            coordinates.put("latitude", latitude);
            coordinates.put("longitude", longitude);

            log.info("변환된 좌표 - 위도: {}, 경도: {}", latitude, longitude);
            return coordinates;

        } catch (Exception e) {
            log.error("카카오 API 호출 실패", e);
            return null;
        }
    }
}
