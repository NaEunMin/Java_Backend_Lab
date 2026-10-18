package com.api.jdbc.repository;

import com.api.spring.di.entity.Product;
import com.api.spring.di.repository.ProductRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class JdbcTemplateProductRepository implements ProductRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Product product) {
        String sql = """
                INSERT INTO products (id, name, price)
                VALUES(?, ?, ?)
                """;
        jdbcTemplate.update(sql, product.getId(), product.getName(), product.getPrice());
    }

    @Override
    public Optional<Product> findById(long id) {
        String sql = """
                SELECT id, name, price
                FROM products
                WHERE id = ?
                """;

        List<Product> products = jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> new Product(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("price")),
                id
        );

        return products.isEmpty() ? Optional.empty() : Optional.of(products.getFirst());
    }
}
