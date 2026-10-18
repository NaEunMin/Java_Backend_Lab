package com.api.springboot.config;

import com.api.jdbc.repository.JdbcProductRepository;
import com.api.spring.di.repository.ProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class JdbcRepositoryConfig {

    @Bean
    @Primary
    public ProductRepository jdbcProductRepository(DataSource dataSource) {
        return new JdbcProductRepository(dataSource); //JDBC 저장소 생성 후 반환
    }
}
