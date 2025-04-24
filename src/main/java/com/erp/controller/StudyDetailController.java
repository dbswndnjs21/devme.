package com.erp.controller;

import com.erp.domain.dto.StudyDetailsDto;
import com.erp.service.StudyService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class StudyDetailController {

    private final StudyService studyService;


    @GetMapping("/study/detail/{id}")
    public String studyDetail(@PathVariable int id) {
        return "studyDetail";
    }

    @GetMapping("/api/studyDetail/{id}")
    @ResponseBody
    public ResponseEntity<?> getStudyDetail(@PathVariable Long id) {
        try {
            StudyDetailsDto studyDetails = studyService.getStudyDetails(id);
            System.out.println("값확인 : " + studyDetails.getStudyDetail().toString());
            return ResponseEntity.ok(studyDetails);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()); // 404 반환
        }
    }
}
