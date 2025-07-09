package com.erp.controller;

import com.erp.entity.Study;
import com.erp.service.StudyListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class StudyListController {

    private final StudyListService studyListService;

    @GetMapping("/studyList")
    public String StudyList() {
        return "studyList";
    }

    @GetMapping("/api/studyList")
    @ResponseBody
    public ResponseEntity<?> getStudies() {
        List<Study> studies = studyListService.getStudyList();
        if (studies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(studies);
    }
}
