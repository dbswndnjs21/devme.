package com.erp.service;

import com.erp.dto.NearbyStudyRequest;
import com.erp.dto.NearbyStudyResponse;
import com.erp.dto.StudyWithDistanceDto;
import com.erp.entity.User;
import com.erp.repository.StudyRepository;
import com.erp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NearbyStudyService {

    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

    /**
     * 사용자 주변 스터디 조회
     */
    public NearbyStudyResponse findNearbyStudies(NearbyStudyRequest request) {
        log.info("근처 스터디 조회 요청 - userId: {}, radius: {}km", 
                request.getUserId(), request.getRadiusKm());

        // 1. 사용자 정보 조회
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + request.getUserId()));

        // 2. 사용자 위치 확인
        if (user.getLatitude() == null || user.getLongitude() == null) {
            throw new IllegalStateException("사용자의 위치 정보가 등록되지 않았습니다. 주소를 먼저 등록해주세요.");
        }

        // 3. 바운딩 박스 계산 (성능 최적화)
        BoundingBox boundingBox = calculateBoundingBox(
                user.getLatitude(), 
                user.getLongitude(), 
                request.getRadiusKm()
        );

        // 4. 근처 스터디 조회
        List<StudyWithDistanceDto> nearbyStudies = studyRepository.findNearbyStudies(
                user.getLatitude(),
                user.getLongitude(),
                boundingBox.getLatMin(),
                boundingBox.getLatMax(),
                boundingBox.getLonMin(),
                boundingBox.getLonMax(),
                request.getRadiusKm(),
                request.getStatus()
        );

        log.info("근처 스터디 {}개 조회됨", nearbyStudies.size());

        // 5. 응답 DTO 생성
        return new NearbyStudyResponse(
                new NearbyStudyResponse.UserLocationDto(
                        user.getLatitude(),
                        user.getLongitude(),
                        user.getAddress()
                ),
                nearbyStudies,
                request.getRadiusKm(),
                nearbyStudies.size()
        );
    }

    /**
     * 두 점 사이의 거리 계산 (하버사인 공식)
     */
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371; // 지구 반지름 (km)

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // 거리 (km)
    }

    /**
     * 바운딩 박스 계산 (성능 최적화를 위한 사각형 영역)
     */
    private BoundingBox calculateBoundingBox(double centerLat, double centerLon, double radiusKm) {
        // 위도 1도 ≈ 111km
        // 경도 1도 ≈ 111km * cos(위도)
        double latRange = radiusKm / 111.0;
        double lonRange = radiusKm / (111.0 * Math.cos(Math.toRadians(centerLat)));

        return new BoundingBox(
                centerLat - latRange,  // latMin
                centerLat + latRange,  // latMax
                centerLon - lonRange,  // lonMin
                centerLon + lonRange   // lonMax
        );
    }

    /**
     * 바운딩 박스 내부 클래스
     */
    private static class BoundingBox {
        private final double latMin;
        private final double latMax;
        private final double lonMin;
        private final double lonMax;

        public BoundingBox(double latMin, double latMax, double lonMin, double lonMax) {
            this.latMin = latMin;
            this.latMax = latMax;
            this.lonMin = lonMin;
            this.lonMax = lonMax;
        }

        public double getLatMin() { return latMin; }
        public double getLatMax() { return latMax; }
        public double getLonMin() { return lonMin; }
        public double getLonMax() { return lonMax; }
    }
}