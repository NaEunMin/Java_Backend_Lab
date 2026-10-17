package com.api.jdbc;

import com.api.jdbc.exception.RepositoryAccessException;
import com.api.jdbc.repository.JdbcProductRepository;
import com.api.spring.di.entity.Product;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcProductRepositoryTest {
    private Connection keepAliveConnection;
    private JdbcProductRepository jdbcProductRepository;

    @BeforeEach
    public void setup() throws SQLException {
        JdbcDataSource dataSource = new JdbcDataSource();
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

        jdbcProductRepository = new JdbcProductRepository(dataSource);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (keepAliveConnection != null) {
            keepAliveConnection.close();
        }
    }

    @Test
    public void 저장한_상품을_조회한다() {
        jdbcProductRepository.save(new Product(1L, "keyboard", 3000));

        Optional<Product> product = jdbcProductRepository.findById(1L);
        assertTrue(product.isPresent());
        assertEquals(1L, product.get().getId());
        assertEquals("keyboard", product.get().getName());
        assertEquals(3000, product.get().getPrice());
    }

    @Test
    public void 없는_상품은_빈_Optional을_반환한다() {
        Optional<Product> product = jdbcProductRepository.findById(999);

        assertTrue(product.isEmpty());
    }

    @Test
    public void 중복_상품은_예외를_발생시키고_기존_상품을_유지한다() {
        jdbcProductRepository.save(new Product(1L, "keyboard", 3000));
        RepositoryAccessException exception = assertThrows(RepositoryAccessException.class, () ->
                jdbcProductRepository.save(new Product(1L, "mouse", 4000)));

        SQLException sqlException = assertInstanceOf(SQLException.class, exception.getCause());
        assertEquals(23505, sqlException.getErrorCode());

        Optional<Product> product = jdbcProductRepository.findById(1L);
        assertTrue(product.isPresent());
        assertEquals(1L, product.get().getId());
        assertEquals("keyboard", product.get().getName());
        assertEquals(3000, product.get().getPrice());
    }

    @Test
    public void SQL에러를_상품없음으로_처리하지_않는다() throws SQLException {
        try (PreparedStatement statement = keepAliveConnection.prepareStatement("DROP TABLE products")) {
            statement.executeUpdate();
        }
        RepositoryAccessException exception = assertThrows(RepositoryAccessException.class, () ->
                jdbcProductRepository.findById(1L));

        assertInstanceOf(SQLException.class, exception.getCause());
    }

}
