// src/main/java/com/erp/config/ElasticsearchConfig.java
package com.erp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.erp.repository")
public class ElasticsearchConfig {
}