package com.api.java.payment;

import com.api.java.payment.exception.OrderPaymentException;
import com.api.java.payment.exception.PaymentGatewayException;

public class PaymentService {
    private final PaymentGateway paymentGateway;

    public PaymentService(PaymentGateway paymentGateway) {
      this.paymentGateway = paymentGateway;
    }

    public String pay(long orderId, int amount){
        try{
            return paymentGateway.pay(orderId, amount);
        } catch (PaymentGatewayException exception){
            throw new OrderPaymentException(orderId, exception);
        }
    }
}
