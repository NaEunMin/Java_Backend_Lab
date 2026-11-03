package com.api.springboot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int price;

    protected ProductEntity() {
    }

    public ProductEntity(Long id, String name, int price) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID는 null이 아닌 양수여야 합니다.");
        }

        if (name == null || name.isBlank() || name.length() > 100) {
            throw new IllegalArgumentException("name은 null이거나 비어있을 수 없고 100자 이하여야 합니다.");
        }

        if (price <= 0) {
            throw new IllegalArgumentException("price는 양수여야 합니다.");
        }
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public void changePrice(int newPrice) {
        if (newPrice <= 0) {
            throw new IllegalArgumentException("새로운 가격은 1 이상이어야 합니다.");
        }
        this.price = newPrice;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
