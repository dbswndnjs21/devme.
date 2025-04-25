package com.erp.controller;

import com.erp.domain.dto.StudyJoinRequestListDto;
import com.erp.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final StudyJoinService studyJoinService;

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

}
