package com.api.java.payment;

import com.api.java.payment.PaymentGateway;
import com.api.java.payment.PaymentService;
import com.api.java.payment.exception.OrderPaymentException;
import com.api.java.payment.exception.PaymentGatewayException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentServiceTest {

    @Test
    public void 서비스가_결제_ID를_그대로_반환하는지_검증() {
        PaymentGateway paymentGateway = new PaymentGateway() {
            @Override
            public String pay(long orderId, int amount) {
                return "PAY-100";
            }
        };

        PaymentService paymentService = new PaymentService(paymentGateway);

        assertEquals("PAY-100", paymentService.pay(100L, 10_000));
    }

    @Test
    public void 예외변환_과_예외가_보존이_되는지_검증() {
        PaymentGatewayException originalException = new PaymentGatewayException("결제 서버 장애 오류");

        PaymentGateway paymentGateway = new PaymentGateway() {
            @Override
            public String pay(long orderId, int amount) {
                throw originalException;
            }
        };

        PaymentService paymentService = new PaymentService(paymentGateway);
        OrderPaymentException exception =
                assertThrows(OrderPaymentException.class,
                        () -> paymentService.pay(100L, 10_000));

        assertEquals(100L, exception.getOrderId());
        assertSame(originalException, exception.getCause());
    }

    @Test
    public void 무관한_예외는_변환하지않고_그대로전파() {
        IllegalArgumentException originalException = new IllegalArgumentException("잘못된 결제 금액");
        PaymentGateway paymentGateway = new PaymentGateway() {
            @Override
            public String pay(long orderId, int amount) {
                throw originalException;
            }
        };

        PaymentService paymentService = new PaymentService(paymentGateway);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                ()-> paymentService.pay(100L, 10_000));

        assertSame(originalException, exception);
    }

}
