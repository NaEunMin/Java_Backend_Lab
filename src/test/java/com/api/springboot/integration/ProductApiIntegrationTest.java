package com.api.springboot.integration;

import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ProductApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;

    @Test
    public void 등록한_상품을_Location으로_조회할_수_있다() throws Exception {

        //상품 저장 및 검증
        MvcResult result = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "id" : 2,
                                        "name" : "keyboard",
                                        "price" : 3000
                                    }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/products/2"))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("keyboard"))
                .andExpect(jsonPath("$.price").value(3000))
                .andReturn();

        String location = result.getResponse().getHeader("Location");
        assertNotNull(location);

        //location으로 상품 조회
        mockMvc.perform(get(location)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("keyboard"))
                .andExpect(jsonPath("$.price").value(3000));
    }

    @Test
    public void 중복_등록을_거부하고_기존_상품을_유지한다() throws Exception {
        //상품 저장 및 검증
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "id" : 2,
                                        "name" : "keyboard",
                                        "price" : 3000
                                    }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/products/2"))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("keyboard"))
                .andExpect(jsonPath("$.price").value(3000))
                .andReturn();

        //ID 중복의 다른 상품 저장 시도
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "id" : 2,
                                        "name" : "mouse",
                                        "price" : 4000
                                    }
                                """
                        ))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("PRODUCT_ALREADY_EXISTS"))
                .andExpect(jsonPath("$.message").value("이미 존재하는 상품입니다."));

        //기존 상품 유지 확인
        mockMvc.perform(get("/products/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("keyboard"))
                .andExpect(jsonPath("$.price").value(3000));
    }

    @Test
    public void HTTP요청으로_등록된_데이터가_DB에_있는지_확인한다() throws Exception {
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id" : 2,
                                    "name" : "mouse",
                                    "price" : 4000
                                }
                                """))
                .andExpect(status().isCreated());

        String select_sql = """
                SELECT id, name, price
                FROM products
                WHERE id = ?
                """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(select_sql)) {
            preparedStatement.setInt(1, 2);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                assertTrue(resultSet.next());
                assertEquals(2, resultSet.getInt("id"));
                assertEquals("mouse", resultSet.getString("name"));
                assertEquals(4000, resultSet.getDouble("price"));
            }
        }
    }

    @Test
    public void 요청데이터의_이름이_100자를_넘어간다면_예외를_반환한다() throws Exception {
        String name = "a".repeat(101);
        MvcResult result = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id" : 2,
                                    "name" : "%s",
                                    "price" : 3000
                                }
                                """.formatted(name)))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException());
    }

    @Test
    public void 요청데이터의_이름이_100자라면_DB에_저장한다() throws Exception {
        String name = "a".repeat(100);
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "id" : 2,
                                "name" : "%s",
                                "price" : 4000
                            }
                        """.formatted(name)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(name));
    }
}
