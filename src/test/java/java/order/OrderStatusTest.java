package java.order;

import java.api.java.order.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class OrderStatusTest {

    @DisplayName("고정된 상수 객체 2개가 같은 인스턴스인지 테스트합니다.")
    @Test
    public void equalEnumInstanceTest() {
        OrderStatus first = OrderStatus.PAID;
        OrderStatus second = OrderStatus.valueOf("PAID");

        assertSame(first, second, "Enum으로 정의된 상수라면 같은 인스턴스여야 합니다.");
    }

    @DisplayName("각 상수들이 올바른 설명글을 가지는지 테스트합니다.")
    @ParameterizedTest(name = "{0}의 설명은 {1}이다.")
    @CsvSource({
            "CREATED, 주문 생성",
            "PAID, 결제 완료",
            "SHIPPED, 배송 중",
            "COMPLETED, 배송 완료",
            "CANCELED, 주문 취소"
    })
    public void equalEnumDescription(OrderStatus orderStatus, String description) {
        assertEquals(description, orderStatus.getDescription());
    }

    @DisplayName("잘못된 상수가 들어갔을 때 예외를 던지는지 테스트합니다")
    @Test
    public void illegalArgumentTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            OrderStatus.valueOf("paid");
        });
    }

    @DisplayName("상태 전이 가능을 테스트합니다.")
    @ParameterizedTest
    @CsvSource({
            "CREATED, PAID, true",
            "CREATED, CANCELED, true",
            "PAID, SHIPPED, true",
            "PAID, CANCELED, true",
            "SHIPPED, COMPLETED, true",
            "COMPLETED, CREATED, false",
            "CANCELED, PAID, false",

            "CREATED, SHIPPED, false",
            "CREATED, COMPLETED, false",
            "CREATED, CREATED, false",

            "PAID, CREATED, false",
            "PAID, COMPLETED, false",
            "PAID, PAID, false",

            "SHIPPED, CANCELED, false",
            "SHIPPED, SHIPPED, false"
    })
    public void canTransitionTest(
            OrderStatus current,
            OrderStatus next,
            boolean expected
    ) {
        boolean actual = current.canTransitionTo(next);

        assertEquals(expected, actual);
    }
}
