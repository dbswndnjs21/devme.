package com.erp.document;

import com.erp.entity.Study;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;

@Document(indexName = "studies")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyDocument {

    @Id
    private String id;

    // N-gram 분석기 적용
    @Field(type = FieldType.Text,
            analyzer = "korean_ngram",
            searchAnalyzer = "korean_search")
    private String title;

    @Field(type = FieldType.Text,
            analyzer = "korean_ngram",
            searchAnalyzer = "korean_search")
    private String description;

    @Field(type = FieldType.Integer)
    private int maxMembers;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Long)
    private Long createdBy;

    @Field(type = FieldType.Date, format = DateFormat.date)
    private LocalDate createdAt;

    @Field(type = FieldType.Text,
            analyzer = "korean_ngram",
            searchAnalyzer = "korean_search")
    private String goal;

    @Field(type = FieldType.Text,
            analyzer = "korean_ngram",
            searchAnalyzer = "korean_search")
    private String tools;

    @Field(type = FieldType.Keyword)
    private String department;

    // Entity -> Document 변환
    public static StudyDocument fromEntity(Study study) {
        return StudyDocument.builder()
                .id(study.getId().toString())
                .title(study.getTitle())
                .description(study.getDescription())
                .maxMembers(study.getMaxMembers())
                .status(study.getStatus().name())
                .createdBy(study.getCreatedBy().getId())
                .createdAt(study.getCreatedAt() != null ? study.getCreatedAt().toLocalDate() : LocalDate.now())
                .goal(study.getDetail() != null ? study.getDetail().getGoal() : "")
                .tools(study.getDetail() != null ? study.getDetail().getTools() : "")
                .department(study.getCreatedBy().getDepartment())
                .build();
    }
}