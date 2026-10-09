package spring.di;

import java.api.spring.di.ProductService;
import java.api.spring.di.config.AmbiguousConfig;
import java.api.spring.di.config.PrimaryConfig;
import java.api.spring.di.config.QualifierConfig;
import java.api.spring.di.entity.Product;
import java.api.spring.di.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

public class BeanSelectionTest {

    @Test
    public void 선택_기준이_없다면_컨테이너_초기화에_실패한다() {
        UnsatisfiedDependencyException exception = assertThrows(UnsatisfiedDependencyException.class, () -> {
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AmbiguousConfig.class);
        });

        assertInstanceOf(NoUniqueBeanDefinitionException.class, exception.getMostSpecificCause());
    }

    @Test
    public void primary저장소에_저장한_상품을_서비스에서_조회한다() {
        try(AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(PrimaryConfig.class)) {
            ProductRepository mainRepository = context.getBean("mainRepository", ProductRepository.class);
            ProductRepository backupRepository = context.getBean("backupRepository", ProductRepository.class);
            ProductService service = context.getBean(ProductService.class);

            Product mainProduct = new Product(1L, "키보드", 1000);
            Product backupProduct = new Product(1L, "마우스", 1000);
            mainRepository.save(mainProduct);
            backupRepository.save(backupProduct);

            Product result = service.getProduct(1L);

            assertSame(mainProduct, result);
        }
    }

    @Test
    public void 명시적_조건에_따라_backup_저장소를_사용한다() {
        try(AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(QualifierConfig.class)) {

            ProductRepository mainRepository = context.getBean("mainRepository", ProductRepository.class);
            ProductRepository backupRepository = context.getBean("backupRepository", ProductRepository.class);
            ProductService service = context.getBean(ProductService.class);

            Product mainProduct = new Product(1L, "키보드", 1000);
            Product backupProduct = new Product(1L, "마우스", 1000);

            mainRepository.save(mainProduct);
            backupRepository.save(backupProduct);

            Product result = service.getProduct(1L);
            assertSame(backupProduct, result);
        }
    }

}
