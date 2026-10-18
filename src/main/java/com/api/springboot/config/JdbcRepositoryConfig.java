package com.api.springboot.config;

import com.api.jdbc.repository.JdbcTemplateProductRepository;
import com.api.spring.di.repository.ProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class JdbcRepositoryConfig {

    @Bean
    @Primary
    public ProductRepository jdbcTemplateProductRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcTemplateProductRepository(jdbcTemplate); //JDBC 저장소 생성 후 반환
    }
}
