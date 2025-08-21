package com.erp.controller;

import com.erp.dto.NearbyStudyRequest;
import com.erp.dto.NearbyStudyResponse;
import com.erp.enums.StudyStatus;
import com.erp.service.NearbyStudyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
public class NearbyStudyController {

    private final NearbyStudyService nearbyStudyService;

    /**
     * 내 주변 스터디 찾기 페이지 (home에서 버튼 클릭시)
     */
    @GetMapping("/nearbyStudies")
    public String nearbyStudiesPage() {
        return "nearStudies"; // templates/nearbyStudies.html
    }

    /**
     * 근처 스터디 조회 API (AJAX 호출용)
     */
    @GetMapping("/api/studies/nearby")
    @ResponseBody
    public ResponseEntity<NearbyStudyResponse> getNearbyStudies(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "5.0") Double radiusKm,
            @RequestParam(required = false) StudyStatus status) {

        try {
            NearbyStudyRequest request = new NearbyStudyRequest();
            request.setUserId(userId);
            request.setRadiusKm(radiusKm);
            request.setStatus(status);

            NearbyStudyResponse response = nearbyStudyService.findNearbyStudies(request);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.error("잘못된 요청: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            log.error("상태 오류: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("근처 스터디 조회 중 오류 발생", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * POST 방식 근처 스터디 조회 API (더 복잡한 요청 처리시)
     */
    @PostMapping("/api/studies/nearby")
    @ResponseBody
    public ResponseEntity<NearbyStudyResponse> findNearbyStudies(
            @Valid @RequestBody NearbyStudyRequest request) {

        try {
            NearbyStudyResponse response = nearbyStudyService.findNearbyStudies(request);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.error("잘못된 요청: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            log.error("상태 오류: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("근처 스터디 조회 중 오류 발생", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 스터디 상세 페이지 (지도에서 스터디 클릭시)
     */
    @GetMapping("/studies/{studyId}")
    public String studyDetail(@PathVariable Long studyId, Model model) {
        model.addAttribute("studyId", studyId);
        return "studyDetail";
    }
}