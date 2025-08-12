package com.erp.service;

import com.erp.document.StudyDocument;
import com.erp.entity.Study;
import com.erp.repository.StudyRepository;
import com.erp.repository.StudySearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudySearchService {

    private final StudySearchRepository studySearchRepository;
    private final StudyRepository studyRepository;

    // 키워드로 스터디 검색
    public Page<StudyDocument> searchStudies(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        if (keyword == null || keyword.trim().isEmpty()) {
            // 키워드가 없으면 전체 조회
            return studySearchRepository.findAll(pageable);
        } else {
            // 키워드 검색
            return studySearchRepository.searchByKeyword(keyword.trim(), pageable);
        }
    }


    // 상태별 필터링
    public Page<StudyDocument> searchByStatus(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return studySearchRepository.findByStatus(status, pageable);
    }

    // 모든 스터디를 Elasticsearch에 인덱싱 (초기 설정용)
    @Transactional(readOnly = true)
    public void indexAllStudies() {
        log.info("모든 스터디 인덱싱 시작...");
        
        List<Study> studies = studyRepository.findAll();
        List<StudyDocument> documents = studies.stream()
                .map(StudyDocument::fromEntity)
                .collect(Collectors.toList());
        
        studySearchRepository.saveAll(documents);
        log.info("총 {}개 스터디 인덱싱 완료", documents.size());
    }


     // 새 스터디 생성 시 인덱싱
    public void indexStudy(Study study) {
        try {
            StudyDocument document = StudyDocument.fromEntity(study);
            studySearchRepository.save(document);
            log.info("스터디 인덱싱 완료: {}", study.getTitle());
        } catch (Exception e) {
            log.error("스터디 인덱싱 실패: {}", study.getTitle(), e);
        }
    }
}