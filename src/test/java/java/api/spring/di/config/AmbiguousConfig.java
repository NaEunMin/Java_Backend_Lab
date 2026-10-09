package java.api.spring.di.config;

import java.api.spring.di.ProductService;
import java.api.spring.di.repository.InMemoryProductRepository;
import java.api.spring.di.repository.ProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmbiguousConfig {

    @Bean
    public ProductRepository mainRepository() {
        return new InMemoryProductRepository();
    }

    @Bean
    public ProductRepository backupRepository(){
        return new InMemoryProductRepository();
    }

    @Bean
    public ProductService productService(ProductRepository productRepository) {
        return new ProductService(productRepository);
    }
}
