package com.api.java.exception;

public class InsufficientStockException extends RuntimeException {
    private final int availableQuantity;
    private final int requestedQuantity;

    public InsufficientStockException(int quantity, int requestedQuantity) {
        super(
                "재고가 부족합니다. 현재 재고: "
                        + quantity
                        + ", 요청 수량: "
                        + requestedQuantity
        );
        this.availableQuantity = quantity;
        this.requestedQuantity = requestedQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }
    public int getRequestedQuantity() {
        return requestedQuantity;
    }
}
