package spring.di;

import java.api.spring.di.ProductService;
import java.api.spring.di.config.ProductConfig;
import java.api.spring.di.entity.Product;
import java.api.spring.di.exception.ProductNotFoundException;
import java.api.spring.di.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

public class ProductConfigTest {
    private AnnotationConfigApplicationContext context;
    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigApplicationContext(ProductConfig.class);
        productRepository = context.getBean(ProductRepository.class);
        productService = context.getBean(ProductService.class);
    }

    @AfterEach
    void tearDown() {
        if(context != null) {
            context.close();
        }
    }

    @Test
    public void 컨테이너에서_조회한_저장소에_저장한_상품을_컨테이너에서_조회한_서비스로_조회한다() {
        Product product = new Product(1L, "키보드", 1000);
        productRepository.save(product);

        Product result = productService.getProduct(1L);
        assertSame(product, result);
    }

    @Test
    public void 같은_컨테이너에서_서비스를_조회하면_같은_객체이다(){
        ProductService resultService = context.getBean(ProductService.class);
        ProductRepository resultRepository = context.getBean(ProductRepository.class);
        assertSame(productService, resultService);
        assertSame(productRepository, resultRepository);
    }

    @Test
    public void 다른_컨테이너에서_만든_메모리_저장소는_공유되지_않는다() {
        AnnotationConfigApplicationContext stubContext = new AnnotationConfigApplicationContext(ProductConfig.class);
        ProductService stubService = stubContext.getBean(ProductService.class);

        Product product = new Product(1L, "키보드", 1000);
        productRepository.save(product);

        Product result = productService.getProduct(1L);
        assertSame(product, result);

        assertThrows(ProductNotFoundException.class, () ->
                stubService.getProduct(1L));

        stubContext.close();
    }
}
