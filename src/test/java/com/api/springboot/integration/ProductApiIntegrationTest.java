package com.api.springboot.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ProductApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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

}
