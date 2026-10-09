package com.api.java.stream.advanced;

import com.api.java.generic.bounded.Identifiable;

import java.util.List;

public class PurchaseOrder implements Identifiable<Long> {
    private final Long id;
    private final List<OrderItem> items;

    public PurchaseOrder(Long id, List<OrderItem> items) {
        this.id = id;
        this.items = List.copyOf(items);
    }

    public List<OrderItem> getItems() {
        return items;
    }

    @Override
    public Long getId() {
        return id;
    }
}
