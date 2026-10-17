package com.api.jdbc;

import com.api.spring.di.entity.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcBasicTest {
    private Connection connection;

    @BeforeEach
    public void setup() throws SQLException {
        String url = "jdbc:h2:mem:" + UUID.randomUUID();
        connection = DriverManager.getConnection(url);

        String sql = """
                CREATE TABLE products (
                    id BIGINT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    price INTEGER NOT NULL
                )
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }


    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void 상품을_INSERT하고_SELECT로_다시_읽는다() throws SQLException {
        String insert_sql = """
                INSERT INTO products(id, name, price)
                VALUES(?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(insert_sql)) {
            statement.setLong(1, 1L);
            statement.setString(2, "keyboard");
            statement.setInt(3, 3000);

            int affectedRows = statement.executeUpdate();

            assertEquals(1, affectedRows);
        }

        String select_sql = """
                SELECT id, name, price
                FROM products
                WHERE id = ?;
                """;

        try (PreparedStatement statement = connection.prepareStatement(select_sql)) {
            statement.setLong(1, 1L);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Product product = new Product(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("price")
                    );

                    assertEquals(1L, product.getId());
                    assertEquals("keyboard", product.getName());
                    assertEquals(3000, product.getPrice());
                    assertFalse(resultSet.next());
                }
            }
        }
    }

    @Test
    public void 없는_상품을_조회하면_결과가_없다() throws SQLException {
        String select_sql = """
                        SELECT id, name, price
                        FROM products
                        WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(select_sql)) {
            statement.setLong(1, 999);

            try (ResultSet resultSet = statement.executeQuery()) {
                assertFalse(resultSet.next());
            }
        }
    }

    @Test
    public void 같은_ID를_INSERT하면_DB가_거부한다() throws SQLException {
        String insert_sql = """
                        INSERT INTO products(id, name, price)
                        VALUES(?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(insert_sql)) {
            statement.setLong(1, 1L);
            statement.setString(2, "keyboard");
            statement.setInt(3, 3000);
            statement.executeUpdate();
        }

        try (PreparedStatement statement = connection.prepareStatement(insert_sql)) {
            statement.setLong(1, 1L);
            statement.setString(2, "mouse");
            statement.setInt(3, 4000);

            SQLException exception = assertThrows(SQLException.class, statement::executeUpdate);
            assertEquals(23505, exception.getErrorCode());
        }
    }
}
