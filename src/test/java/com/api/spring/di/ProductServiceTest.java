package com.api.spring.di;


import com.api.spring.di.service.ProductService;
import com.api.spring.di.entity.Product;
import com.api.spring.di.exception.ProductNotFoundException;
import com.api.spring.di.repository.InMemoryProductRepository;
import com.api.spring.di.repository.ProductRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {
    private final ProductService productService = new ProductService(new InMemoryProductRepository());

    @Test
    public void 상품을_저장하고_저장소에서_상품을_조회한다() {
        //given
        Product product = new Product(1L, "키보드", 1000);
        productService.save(product);

        //when
        Product result = productService.getProduct(1L);

        //then
        assertEquals(1L, result.getId());
        assertEquals("키보드", result.getName());
    }

    @Test
    public void 상품이_존재하지_않으면_예외를_반환한다() {
        assertThrows(ProductNotFoundException.class, ()
                -> productService.getProduct(1L));
    }

    @Test
    public void 서비스코드를_교체하지않고_상품을_조회한다() {
        Product expected = new Product(1L, "키보드", 1000);
        ProductService stubProductService = new ProductService(new StubProductRepository(expected));
        Product result = stubProductService.getProduct(1L);

        assertSame(expected, result);
    }



    private static class StubProductRepository
            implements ProductRepository {

        private final Product product;

        StubProductRepository(Product product) {
            this.product = product;
        }

        @Override
        public Optional<Product> findById(long id) {
            if (product.getId() == id) {
                return Optional.of(product);
            }
            return Optional.empty();
        }

        @Override
        public void save(Product product) {
            throw new UnsupportedOperationException(
                    "이 테스트용 저장소는 조회만 지원합니다."
            );
        }
    }
}
