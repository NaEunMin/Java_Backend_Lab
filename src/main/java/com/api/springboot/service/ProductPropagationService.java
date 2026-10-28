package com.api.springboot.service;

import com.api.spring.di.entity.Product;
import com.api.spring.di.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductPropagationService {
    private final ProductRepository productRepository;
    private final ProductSaveService productSaveService;

    public ProductPropagationService(ProductRepository productRepository, ProductSaveService productSaveService) {
        this.productRepository = productRepository;
        this.productSaveService = productSaveService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveTwo(Product first, Product second) {
        productRepository.save(first);
        productSaveService.save(second);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveTwoThenFail(Product first, Product second) {
        productRepository.save(first);
        productSaveService.save(second);
        throw new IllegalStateException();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveTwoInnerFailure(Product first, Product second) {
        productRepository.save(first);
        productSaveService.saveThenFail(second);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean saveTwoAndCatchInnerFailure(Product first, Product second) {
        /*
            안쪽 서비스의 런타임 예외가 안쪽 트랜잭션 프록시까지 전달되면 공유 트랜잭션에 rollback-only가 설정된다.
            바깥 메서드가 예외를 잡고 false를 반환해도 이미 설정된 rollback-only는 사라지지 않는다.
            바깥 트랜잭션 프록시는 종료 처리 중 실제 롤백을 수행하고 UnexpectedRollbackException을 던진다.
            따라서 외부 호출자는 false가 아니라 UnexpectedRollbackException을 받는다.
         */
        productRepository.save(first);
        try {
            productSaveService.saveThenFail(second);
        } catch (IllegalStateException e) {
            return false;
        }
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean saveTwoAndCatchLocalFailure(Product first, Product second) {
        /*
            안쪽 서비스가 정상 종료된 뒤 바깥 메서드에서 직접 던진 예외를 같은 메서드 안에서 잡는다.
            이 예외는 트랜잭션 프록시에 전달되지 않으며, 이번 코드에서는 rollback-only도 설정되지 않는다.
            바깥 트랜잭션 프록시는 메서드의 정상 반환을 확인하고 커밋을 마친 뒤 외부 호출자에게 false를 전달한다.
            프록시는 반환값 false의 의미를 해석해서 커밋 여부를 결정하는 것이 아니다.
         */
        productRepository.save(first);
        try {
            productSaveService.save(second);
            throw new IllegalStateException();
        } catch (IllegalStateException e) {
            return false;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveWithNewTransactionThenFail(Product first, Product second) {
        /*
            안쪽 성공 후 바깥 트랜잭션에서 실패
         */
        productRepository.save(first);
        productSaveService.saveInNewTransaction(second);
        throw new IllegalStateException();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean saveAndCatchNewTransactionFailure(Product first, Product second) {
        /*
            안쪽 실패를 바깥에서 처리
         */
        productRepository.save(first);
        try {
            productSaveService.saveThenFailInNewTransaction(second);
        } catch (IllegalStateException e) {
            return false;
        }
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveWithNewTransactionFailure(Product first, Product second) {
        /*
            안쪽 실패를 그대로 전파
         */
        productRepository.save(first);
        productSaveService.saveThenFailInNewTransaction(second);
    }

}
