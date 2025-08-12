package com.erp.document;

import com.erp.entity.Study;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Document(indexName = "studies")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyDocument {
    
    @Id
    private String id;
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String description;
    
    @Field(type = FieldType.Integer)
    private Integer maxMembers;
    
    @Field(type = FieldType.Keyword)
    private String status;
    
    @Field(type = FieldType.Text)
    private String createdByUsername;
    
    @Field(type = FieldType.Date)
    private LocalDateTime createdAt;
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String goal;
    
    @Field(type = FieldType.Text, analyzer = "standard")
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
                .createdByUsername(study.getCreatedBy().getUsername())
                .createdAt(study.getCreatedAt())
                .goal(study.getDetail() != null ? study.getDetail().getGoal() : "")
                .tools(study.getDetail() != null ? study.getDetail().getTools() : "")
                .department(study.getCreatedBy().getDepartment())
                .build();
    }
}