package com.api.jdbc.repository;

import com.api.jdbc.exception.RepositoryAccessException;
import com.api.spring.di.entity.Product;
import com.api.spring.di.repository.ProductRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcProductRepository implements ProductRepository {
    private final DataSource dataSource;

    public JdbcProductRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Product product) {
        String insert_sql = """
                INSERT INTO products (id, name, price)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(insert_sql)) {
            statement.setLong(1, product.getId());
            statement.setString(2, product.getName());
            statement.setInt(3, product.getPrice());
            statement.executeUpdate();

        } catch (SQLException exception) {
            throw new RepositoryAccessException("상품 저장에 실패했습니다.", exception);
        }

    }

    @Override
    public Optional<Product> findById(long id) {
        String select_sql = """
                SELECT id, name, price
                FROM products
                WHERE id = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(select_sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Product product = new Product(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("price"));
                    return Optional.of(product);
                }
            }
        } catch (SQLException exception) {
            throw new RepositoryAccessException("상품 조회에 실패했습니다.", exception);
        }
        return Optional.empty();
    }
}
