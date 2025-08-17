package com.erp.service;

import com.erp.document.StudyDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
// 인덱스 설정
public class ElasticsearchIndexService {

    private final ElasticsearchOperations elasticsearchOperations;

    // @PostConstruct 제거 - 필요할 때만 수동 실행
    public void initializeIndex() {
        IndexCoordinates indexCoordinates = IndexCoordinates.of("studies");

        // 기존 인덱스 삭제
        if (elasticsearchOperations.indexOps(indexCoordinates).exists()) {
            elasticsearchOperations.indexOps(indexCoordinates).delete();
            log.info("기존 studies 인덱스 삭제 완료");
        }

        // Elasticsearch 8.x 호환 설정
        Map<String, Object> settings = createIndexSettings();

        elasticsearchOperations.indexOps(indexCoordinates).create(settings);
        log.info("N-gram 설정이 적용된 studies 인덱스 생성 완료");

        // 매핑 적용
        elasticsearchOperations.indexOps(StudyDocument.class).putMapping();
        log.info("studies 인덱스 매핑 적용 완료");
    }

    private Map<String, Object> createIndexSettings() {
        Map<String, Object> settings = new HashMap<>();

        // Analysis 설정
        Map<String, Object> analysis = new HashMap<>();

        // Tokenizer 설정
        Map<String, Object> tokenizer = new HashMap<>();
        Map<String, Object> noriTokenizer = new HashMap<>();
        noriTokenizer.put("type", "nori_tokenizer");
        noriTokenizer.put("decompound_mode", "mixed");
        tokenizer.put("nori_tokenizer", noriTokenizer);

        // Filter 설정
        Map<String, Object> filter = new HashMap<>();
        Map<String, Object> edgeNgramFilter = new HashMap<>();
        edgeNgramFilter.put("type", "edge_ngram");
        edgeNgramFilter.put("min_gram", 2);
        edgeNgramFilter.put("max_gram", 10);
        filter.put("edge_ngram_filter", edgeNgramFilter);

        // Analyzer 설정
        Map<String, Object> analyzer = new HashMap<>();

        // Korean N-gram Analyzer
        Map<String, Object> koreanNgram = new HashMap<>();
        koreanNgram.put("type", "custom");
        koreanNgram.put("tokenizer", "nori_tokenizer");
        koreanNgram.put("filter", List.of("lowercase", "edge_ngram_filter"));
        analyzer.put("korean_ngram", koreanNgram);

        // Korean Search Analyzer
        Map<String, Object> koreanSearch = new HashMap<>();
        koreanSearch.put("type", "custom");
        koreanSearch.put("tokenizer", "nori_tokenizer");
        koreanSearch.put("filter", List.of("lowercase"));
        analyzer.put("korean_search", koreanSearch);

        analysis.put("tokenizer", tokenizer);
        analysis.put("filter", filter);
        analysis.put("analyzer", analyzer);

        settings.put("analysis", analysis);

        return settings;
    }

    // 개발/테스트용 - 수동으로 인덱스 초기화
    public void resetIndexForDevelopment() {
        log.warn("개발환경 인덱스 초기화 실행");
        initializeIndex();
    }
}