package com.erp.controller;

import com.erp.dto.*;
import com.erp.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final StudyJoinService studyJoinService;
    private final StudyMemberService studyMemberService;
    private final UserService userService;

    @GetMapping("/myPage")
    public String myPage() {
        return "myPage";
    }

    @GetMapping("/api/myManagedStudies")
    @ResponseBody
    public List<StudyJoinRequestListDto> StudyJoinRequestList(Principal principal) {
        // 내가 찾아야하는 값 : 생성자에게 신청한 목록 -> joinRequest
        return studyJoinService.getStudyJoinRequestList(principal.getName());
    }

    // 스터디 리더가 보는 페이지
    // 내가 만든 스터디에 대해서 신청 현황이 보임
    @GetMapping("/api/myStudyList")
    @ResponseBody
    public List<StudyDto> StudyList(Principal principal) {
        return studyMemberService.getMyStudyList(principal.getName());
    }


    @PostMapping("/api/approveRequest/{requestId}")
    @ResponseBody
    public void approveRequest(@PathVariable Long requestId) {
        studyJoinService.approveRequest(requestId);
    }

    @PostMapping("/api/rejectRequest/{requestId}")
    @ResponseBody
    public void rejectRequest(@PathVariable Long requestId) {
        studyJoinService.rejectRequest(requestId);
    }

    @GetMapping("/study/main/{id}")
    public String studyMainForm(@PathVariable Long id) {
        return "studyMain";
    }

    @GetMapping("/api/myProfile")
    @ResponseBody
    public ApiResponse<UserProfileResponseDto> getMyProfileSimple(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        try {
            String username = customUserDetails.getUsername();
            UserProfileResponseDto profile = userService.getMyProfile(username);
            return ApiResponse.success("내 정보 조회 성공", profile);
        } catch (Exception e) {
            return ApiResponse.fail("내 정보 불러오기 실패: " + e.getMessage());
        }
    }

    /**
     * 내 프로필 수정
     */
    @PutMapping("/api/updateProfile")
    @ResponseBody
    public ApiResponse<?> updateProfileSimple(@RequestBody UserProfileRequestDto requestDto,
                                                 @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        try {
            String username = customUserDetails.getUsername();
            userService.updateProfile(username, requestDto);
            return ApiResponse.success("정보가 수정되었습니다.");
        } catch (Exception e) {
            return ApiResponse.fail("정보 수정 실패: " + e.getMessage());
        }
    }
}
