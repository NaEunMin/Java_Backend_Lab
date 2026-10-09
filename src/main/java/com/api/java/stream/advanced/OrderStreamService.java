package com.api.java.stream.advanced;

import java.util.List;

public class OrderStreamService {

    //중복 제거 이름 찾기
    public List<String> findDistinctProductNames(
            List<PurchaseOrder> orders
    ) {
        validateOrders(orders);
        return orders.stream()
                .flatMap(order -> order.getItems().stream())
                .map(OrderItem::getProductName)
                .distinct()
                .sorted()
                .toList();

    }

    //전체 상품 합계
    public long calculateTotalSales(
            List<PurchaseOrder> orders
    ) {
        validateOrders(orders);
        return orders.stream()
                .flatMap(order -> order.getItems().stream())
                .mapToLong(item -> (long) item.getPrice() * item.getQuantity())
                .sum();
    }

    //고가 상품 존재 여부
    public boolean containsExpensiveItem(
            List<PurchaseOrder> orders,
            int minimumPrice
    ) {
        validateOrders(orders);
        if (minimumPrice < 0) {
            throw new IllegalArgumentException("기준 가격은 음수일 수 없습니다.");
        }
        return orders.stream()
                .flatMap(order -> order.getItems().stream())
                .anyMatch(orderItem -> orderItem.getPrice() >= minimumPrice);
    }

    //상품 존재 여부
    public boolean allOrdersHaveItems(
            List<PurchaseOrder> orders
    ) {
        validateOrders(orders);
        return orders.stream()
                .allMatch(order -> !order.getItems().isEmpty());
    }

    private void validateOrders(List<PurchaseOrder> orders) {
        if (orders == null) {
            throw new IllegalArgumentException("주문은 비어 있을 수 없습니다.");
        }
    }
}
