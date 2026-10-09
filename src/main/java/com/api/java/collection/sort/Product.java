package com.api.java.collection.sort;

public class Product implements Comparable<Product>{
    private final Long id;
    private final String name;
    private final int price;
    private final int stock;

    public Product(Long id, String name, int price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }


    public int getPrice() {
        return price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    @Override
    public int compareTo(Product o) {
        return Long.compare(this.id, o.id);
    }
}
