package com.erp.repository;

import com.erp.document.StudyDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface StudySearchRepository extends ElasticsearchRepository<StudyDocument, String> {
    
    // 제목과 설명에서 키워드 검색
    Page<StudyDocument> findByTitleContainingOrDescriptionContaining(
        String title, String description, Pageable pageable);
    
    // 상태별 필터링
    Page<StudyDocument> findByStatus(String status, Pageable pageable);
    
    // 멀티 필드 검색 (제목, 설명, 목표, 도구에서 검색)
    @Query("""
        {
          "multi_match": {
            "query": "?0",
            "fields": ["title^3", "description^2", "goal", "tools"],
            "type": "best_fields",
            "fuzziness": "AUTO"
          }
        }
        """)
    Page<StudyDocument> searchByKeyword(String keyword, Pageable pageable);

    // 한글 부분검색을 위한 새로운 메서드 추가
    @Query("""
        {
          "bool": {
            "should": [
              {
                "match_phrase": {
                  "title": {
                    "query": "?0",
                    "analyzer": "korean_search",
                    "boost": 3
                  }
                }
              },
              {
                "match": {
                  "description": {
                    "query": "?0",
                    "analyzer": "korean_search",
                    "boost": 2
                  }
                }
              },
              {
                "match": {
                  "goal": {
                    "query": "?0",
                    "analyzer": "korean_search"
                  }
                }
              },
              {
                "match": {
                  "tools": {
                    "query": "?0",
                    "analyzer": "korean_search"
                  }
                }
              }
            ],
            "minimum_should_match": 1
          }
        }
        """)
    Page<StudyDocument> searchByKoreanKeyword(String keyword, Pageable pageable);

}