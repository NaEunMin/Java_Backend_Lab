package com.api.java.payment;

public interface PaymentGateway {
    String pay(long orderId, int amount);
}
