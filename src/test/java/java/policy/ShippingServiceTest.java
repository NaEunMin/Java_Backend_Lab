package java.policy;

import java.api.java.policy.ShippingService;
import java.api.java.policy.StandardPolicy;
import java.api.java.policy.VipPolicy;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShippingServiceTest {

    @ParameterizedTest
    @CsvSource({
            "0, 3000",
            "49999,3000",
            "50000, 0",
            "100000, 0"
    })
    void Standard등급별_수수료를_테스트(int orderAmount, int expectedFee) {

        //given
        ShippingService shippingService = new ShippingService(new StandardPolicy());

        //when
        int fee = shippingService.calculateFee(orderAmount);

        //then
        assertEquals(expectedFee, fee, "Standard등급은 주문금액이 5만원 이상일 때 수수료가 0원입니다.");//5만원 이상인지 테스트
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 49_999, 50_000, 1_000_000})
    void VIP등급일때_수수료가_0원이다(int orderAmount) {

        //given
        ShippingService shippingService = new ShippingService(new VipPolicy());

        //when
        int fee = shippingService.calculateFee(orderAmount);

        //then
        assertEquals(0, fee, "VIP는 항상 수수료가 0원입니다.");
    }
}
