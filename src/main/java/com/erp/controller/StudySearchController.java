package com.erp.controller;

import com.erp.document.StudyDocument;
import com.erp.dto.ApiResponse;
import com.erp.service.StudySearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class StudySearchController {

    private final StudySearchService studySearchService;

    @GetMapping("/studies")
    public ApiResponse<Page<StudyDocument>> searchStudies(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<StudyDocument> results;
        
        if (status != null && !status.trim().isEmpty()) {
            results = studySearchService.searchByStatus(status, page, size);
        } else {
            results = studySearchService.searchStudies(keyword, page, size);
        }

        return ApiResponse.success("검색 완료", results);
    }

    @PostMapping("/reindex")
    public ApiResponse<Void> reindexAllStudies() {
        studySearchService.indexAllStudies();
        return ApiResponse.success("재인덱싱 완료", null);
    }
}