package com.api.jdbc;

import com.api.jdbc.repository.JdbcTemplateProductRepository;
import com.api.spring.di.entity.Product;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcTemplateProductRepositoryTest {

    private Connection keepAliveConnection;
    private JdbcDataSource dataSource = new JdbcDataSource();
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    private JdbcTemplateProductRepository jdbcTemplateProductRepository;

    @BeforeEach
    public void setup() throws SQLException {
        dataSource.setUrl("jdbc:h2:mem:" + UUID.randomUUID());
        dataSource.setUser("sa");
        dataSource.setPassword("");
        keepAliveConnection = dataSource.getConnection();

        String sql = """
                CREATE TABLE products (
                    id BIGINT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    price INTEGER NOT NULL
                )
                """;

        try (PreparedStatement statement = keepAliveConnection.prepareStatement(sql)) {
            statement.executeUpdate();
        }

        jdbcTemplateProductRepository = new JdbcTemplateProductRepository(jdbcTemplate);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (keepAliveConnection != null) {
            keepAliveConnection.close();
        }
    }

    @Test
    public void JdbcTemplate로_상품을_저장하고_조회한다() {
        Product product = new Product(2L, "keyboard", 3000);
        jdbcTemplateProductRepository.save(product);

        Optional<Product> result = jdbcTemplateProductRepository.findById(2L);
        assertTrue(result.isPresent());
        assertEquals(2L, result.get().getId());
        assertEquals("keyboard", result.get().getName());
        assertEquals(3000, result.get().getPrice());
    }

    @Test
    public void 없는_상품을_조회하면_빈Optional을_반환한다() {
        Optional<Product> result = jdbcTemplateProductRepository.findById(2L);
        assertFalse(result.isPresent());
    }

    @Test
    public void 동일한_ID로_다른_상품을_저장한다() {
        Product expect = new Product(2L, "keyboard", 3000);
        jdbcTemplateProductRepository.save(expect);

        Product product = new Product(2L, "mouse", 4000);
        assertThrows(DuplicateKeyException.class, () ->
                jdbcTemplateProductRepository.save(product));

        Optional<Product> result =  jdbcTemplateProductRepository.findById(2L);
        assertTrue(result.isPresent());
        assertEquals(2L, result.get().getId());
        assertEquals("keyboard", result.get().getName());
        assertEquals(3000, result.get().getPrice());
    }

    @Test
    public void 테이블을_삭제_후_조회한다() {
        jdbcTemplate.execute("DROP TABLE products");
        assertThrows(DataAccessException.class, () ->
                jdbcTemplateProductRepository.findById(1L));
    }
}
