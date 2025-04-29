package com.erp.controller;


import com.erp.domain.dto.StudyMainDto;
import com.erp.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class StudyMainController {

    private final StudyService studyService;

    @GetMapping("/api/study/main/{studyId}")
    public ResponseEntity<StudyMainDto> getStudyMain(@PathVariable Long studyId) {
        return ResponseEntity.ok(studyService.getStudyMainInfo(studyId));
    }

}
