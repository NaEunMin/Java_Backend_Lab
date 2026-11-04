package com.api.springboot.repository;

import com.api.springboot.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByPriceBetweenOrderByPriceAscIdAsc(int min, int max);

    @Query("""
            select p
            from ProductEntity p
            where p.price >= :min and p.price <= :max
            order by p.price asc, p.id asc
            """)
    List<ProductEntity> findInPriceRange(@Param("min") int min, @Param("max") int max);
}
