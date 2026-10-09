package com.api.java.policy;

import com.api.java.policy.GradePolicy;
import com.api.java.policy.ShippingService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShippingServiceAnonymousClassTest {

    @Test
    public void 익명_클래스로_이벤트_배송정책을_주입한다(){
        int freeShippingThreshold = 30_000;
        int eventShippingFee = 1_500;

        GradePolicy gradePolicy = new GradePolicy() {
            @Override
            public int amountPolicy(int orderAmount) {
                if(orderAmount >= freeShippingThreshold){
                    return 0;
                }
                return eventShippingFee;
            }
        };

        ShippingService shippingService = new ShippingService(gradePolicy);
        assertEquals(1500, shippingService.calculateFee(29_999), "주문금액이 3만원 미만이면 배송비는 1500원입니다.");
        assertEquals(0, shippingService.calculateFee(30_000), "주문금액이 3만원 이상이면 배송비는 무료입니다.");

    }
}
