package com.api.java.order;

import com.api.java.order.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    @DisplayName("product에 상품을 추가했을 때 Order 객체에 변화가 없어야한다")
    @Test
    public void addProductTest() {
        List<String> products = new ArrayList<>();
        products.add("상품1");

        Order order = new Order(1L, products);

        products.add("상품2");


        assertEquals(1, order.getProducts().size(), "products에 상품을 추가해도 order에는 추가되면 안된다.");

        assertEquals(List.of("상품1"), order.getProducts());
    }

    @DisplayName("getter로 상품을 얻어 추가하려하면 예외가 발생해야한다")
    @Test
    public void getterProductTest() {
        List<String> products = new ArrayList<>();
        products.add("상품1");

        Order order = new Order(1L, products);

        assertThrows(UnsupportedOperationException.class, () -> {
            order.getProducts().add("상품2");
        }, "order에서 getter를 사용하려 product를 얻을 수 없습니다.");
    }
}
