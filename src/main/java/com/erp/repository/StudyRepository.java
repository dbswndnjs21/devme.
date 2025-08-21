package com.erp.repository;

import com.erp.dto.StudyWithDistanceDto;
import com.erp.entity.Study;
import com.erp.entity.User;
import com.erp.enums.StudyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyRepository extends JpaRepository<Study, Long> {

    List<Study> findByCreatedBy(User CreatedBy);

    /**
     * 사용자 위치 기반으로 반경 내 스터디 조회 (하버사인 공식 사용)
     * 바운딩 박스를 먼저 적용하고 정확한 거리 계산
     */
    @Query(value = """
    SELECT s.id, s.title, s.description, s.max_members, s.status, u.username as created_by,
           s.created_at, s.location_address, s.location_latitude, s.location_longitude,
           ROUND(
               6371 * acos(
                   cos(radians(:userLat)) 
                   * cos(radians(s.location_latitude)) 
                   * cos(radians(s.location_longitude) - radians(:userLon)) 
                   + sin(radians(:userLat)) 
                   * sin(radians(s.location_latitude))
               ), 2
           ) AS distance
    FROM study s
    JOIN users u ON s.created_by = u.id
    WHERE s.location_latitude BETWEEN :latMin AND :latMax
      AND s.location_longitude BETWEEN :lonMin AND :lonMax
      AND (:status IS NULL OR s.status = :status)
      AND (6371 * acos(
            cos(radians(:userLat)) 
            * cos(radians(s.location_latitude)) 
            * cos(radians(s.location_longitude) - radians(:userLon)) 
            + sin(radians(:userLat)) 
            * sin(radians(s.location_latitude))
      )) <= :radiusKm
    ORDER BY distance ASC
    """, nativeQuery = true)
    List<StudyWithDistanceDto> findNearbyStudies(
            @Param("userLat") double userLat,
            @Param("userLon") double userLon,
            @Param("latMin") double latMin,
            @Param("latMax") double latMax,
            @Param("lonMin") double lonMin,
            @Param("lonMax") double lonMax,
            @Param("radiusKm") double radiusKm,
            @Param("status") StudyStatus status);


    /**
     * 간단한 조회: 바운딩 박스만 사용 (성능 우선시할 때)
     */
    @Query("SELECT s FROM Study s " +
            "WHERE s.locationLatitude IS NOT NULL " +
            "AND s.locationLongitude IS NOT NULL " +
            "AND s.locationLatitude BETWEEN :latMin AND :latMax " +
            "AND s.locationLongitude BETWEEN :lonMin AND :lonMax " +
            "AND (:status IS NULL OR s.status = :status)")
    List<Study> findStudiesInBoundingBox(
            @Param("latMin") Double latMin,
            @Param("latMax") Double latMax,
            @Param("lonMin") Double lonMin,
            @Param("lonMax") Double lonMax,
            @Param("status") StudyStatus status
    );
}
