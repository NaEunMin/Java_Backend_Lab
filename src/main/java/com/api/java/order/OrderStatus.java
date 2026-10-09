package com.api.java.order;

public enum OrderStatus {
    CREATED("주문 생성"),
    PAID("결제 완료"),
    SHIPPED("배송 중"),
    COMPLETED("배송 완료"),
    CANCELED("주문 취소");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean canTransitionTo(OrderStatus next){
        return switch (this){
            case CREATED -> next == PAID || next == CANCELED;
            case PAID -> next == SHIPPED || next == CANCELED;
            case SHIPPED -> next == COMPLETED;
            case COMPLETED, CANCELED -> false;
        };
    }
}
