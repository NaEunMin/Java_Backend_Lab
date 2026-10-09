package java.api.spring.di;

import java.api.spring.di.entity.Product;
import java.api.spring.di.exception.ProductNotFoundException;
import java.api.spring.di.repository.ProductRepository;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void save(Product product){
        if(product == null){
            throw new IllegalArgumentException("상품은 null일 수 없습니다.");
        }

        productRepository.save(product);
    }

    public Product getProduct(long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("조회 ID : " + id + " 상품이 존재하지 않습니다."));
    }
}
