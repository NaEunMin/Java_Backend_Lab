package com.api.springboot.web.controller;

import com.api.spring.di.entity.Product;
import com.api.spring.di.exception.DuplicateProductException;
import com.api.spring.di.exception.ProductNotFoundException;
import com.api.spring.scan.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class) //MVC 관련 구성에 집중하여 테스트 환경을 만든다.
public class ProductControllerTest {

    @Autowired //준비된 mockMvc를 주입받는다.
    private MockMvc mockMvc;

    @MockitoBean //테스트용 Spring 컨테이너에 가짜 서비스 빈을 컨트롤러에 주입한다.
    private ProductService productService;

    @Test
    public void 상품을_조회하면_200과_상품정보를_반환한다() throws Exception {
        //given
        Product product = new Product(1L, "키보드", 1000);
        when(productService.getProduct(1L)).thenReturn(product);

        //when
        mockMvc.perform(get("/products/{productId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("키보드"))
                .andExpect(jsonPath("$.price").value(1000));

        verify(productService).getProduct(1L);
    }

    @Test
    public void 상품이_존재하지_않으면_404를_반환한다() throws Exception {
        //given : 서비스가 999L로 호출되면 예외를 던지도록 설정 -> 내부 예외 메시지 외부용 메시지를 분리한
        when(productService.getProduct(999L))
                .thenThrow(new ProductNotFoundException("조회 ID : 999 상품이 존재하지 않습니다."));

        //when & then : 요청을 보내고 응답을 검증
        mockMvc.perform(get("/products/{productId}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("PRODUCT NOT FOUND"))
                .andExpect(jsonPath("$.message").value("상품을 찾지 못했습니다."));

        //서비스가 999L로 한번 호출되었는지 검증
        verify(productService).getProduct(999L);
    }

    @Test
    public void 매개변수_타입이_잘못되었다면_서비스를_호출하지_않는다() throws Exception {
        //이 테스트 상황은 service에 도달하지 않으니 service 설정이 불필요하다

        //when & then
        mockMvc.perform(get("/products/{productId}", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID PARAMETER TYPE"))
                .andExpect(jsonPath("$.message").value("요청 값이 형식과 올바르지 않습니다."));


        //서비스를 호출하지 않았는지 확인
        verifyNoInteractions(productService);
    }

    @Test
    public void 상품을_정상적으로_등록한다() throws Exception {
        //given
        Product product = new Product(2L, "마우스", 3000);
        when(productService.createProduct(2L, "마우스", 3000)).thenReturn(product);

        //when & then
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id" : 2,
                                    "name" : "마우스",
                                    "price" : 3000
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/products/2"))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("마우스"))
                .andExpect(jsonPath("$.price").value(3000));


        //서비스를 1번 호출했는지 확인
        verify(productService).createProduct(2, "마우스", 3000);
    }

    @Test
    public void 상품_이름이_공백이면_예외를_반환한다() throws Exception {
        //when & then
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id" : 1,
                                    "name" : "",
                                    "price" : 3000
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("요청 값이 조건을 만족하지 않습니다."));

        verifyNoInteractions(productService);
    }

    @Test
    public void 가격이_0_또는_음수이면_예외를_반환한다() throws Exception {
        //when & then
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id" : 1,
                                    "name" : "keyboard",
                                    "price" : -5
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("요청 값이 조건을 만족하지 않습니다."));

        verifyNoInteractions(productService);
    }

    @Test
    public void 가격이_누락되거나_null이면_예외를_반환한다() throws Exception {
        //when & then
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "id" : 1,
                            "name" : "keyboard"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("요청 값이 조건을 만족하지 않습니다."));

        verifyNoInteractions(productService);
    }

    @Test
    public void 가격에_문자열이_들어오면_예외를_반환한다() throws Exception {
        //when & then
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "id" : 1,
                            "name" : "keyboard",
                            "price" : "abc"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST_BODY"))
                .andExpect(jsonPath("$.message").value("요청 본문의 형식이 올바르지 않습니다."));
        verifyNoInteractions(productService);
    }

    @Test
    public void 중복된_상품을_저장하면_예외를_반환한다() throws Exception {
        //given
        Product product = new Product(1L, "keyboard", 3000);
        when(productService.createProduct(1L, "keyboard", 3000)).thenThrow(new DuplicateProductException("저장된 상품 존재"));

        //when & then
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "id" : 1,
                            "name" : "keyboard",
                            "price" : 3000
                        }
                        """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("PRODUCT_ALREADY_EXISTS"))
                .andExpect(jsonPath("$.message").value("이미 존재하는 상품입니다."));

        verify(productService).createProduct(1L, "keyboard", 3000);
    }

}
