package com.erp.controller;

import com.erp.service.ElasticsearchIndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DevController {
    
    private final ElasticsearchIndexService indexService;
    
    @PostMapping("/dev/reset-index")
    public String resetIndex() {
        indexService.resetIndexForDevelopment();
        return "인덱스 초기화 완료";
    }
}