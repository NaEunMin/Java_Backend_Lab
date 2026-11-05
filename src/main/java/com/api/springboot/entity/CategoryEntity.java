package com.api.springboot.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class CategoryEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "category")
    @JoinColumn(name = "product_id")
    private List<ProductEntity> products = new ArrayList<>();

    public List<ProductEntity> getProducts() {
        return products;
    }

    protected CategoryEntity() {}

    public CategoryEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
