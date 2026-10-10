package com.api.spring.config;

import com.api.spring.di.entity.Product;
import com.api.spring.di.repository.ProductRepository;
import com.api.spring.scan.config.ScanConfig;
import com.api.spring.scan.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.*;

public class ScanConfigTest {

    @Test
    public void 스캔으로_등록_저장소와_서비스가_연결된다() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ScanConfig.class)) {
            ProductService productService = context.getBean(ProductService.class);
            ProductRepository productRepository = context.getBean(ProductRepository.class);

            Product product = new Product(1L, "키보드", 1000);
            productRepository.save(product);

            Product result = productService.getProduct(1L);
            assertSame(product, result); //같은 객체를 리턴하는지 확인해야 하므로 Same 사용
        }
    }

    @Test
    public void 서비스만_스캔하면_필요한_저장소가_없어_초기화에_실패한다() {
        UnsatisfiedDependencyException exception = assertThrows(UnsatisfiedDependencyException.class, () -> {
            try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ServiceOnlyScan.class)) {
            }
        });

        assertEquals(NoSuchBeanDefinitionException.class, exception.getMostSpecificCause().getClass());
    }

    @Configuration
    @ComponentScan(basePackages = "com.api.spring.scan.service")
    static class ServiceOnlyScan {
    }
}
