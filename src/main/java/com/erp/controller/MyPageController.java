package com.erp.controller;

import com.erp.dto.StudyDto;
import com.erp.dto.StudyJoinRequestListDto;
import com.erp.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final StudyJoinService studyJoinService;
    private final StudyMemberService studyMemberService;

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
}
