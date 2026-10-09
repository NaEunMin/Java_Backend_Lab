package java.nested;

import java.api.java.nested.Order;
import java.api.java.nested.OrderMessageService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderNestedClassTest {

    @Test
    public void 배송금액이_50000원_이상이면_배송비는_무료() {
        Order.DeliveryPolicy deliverPolicy = new Order.DeliveryPolicy(50000, 3000);

        assertEquals(3000, deliverPolicy.calculateFee(49999), "주문금액이 5만원 미만이라면 배송비는 3000원입니다");
        assertEquals(0, deliverPolicy.calculateFee(50000), "주문금액이 5만원 이상이라면 배송비는 무료입니다.");
    }

    @Test
    public void 서로_다른_Order에서_생성한_Receipt는_각_Order의_값을_사용한다() {
        Order order1 = new Order(1L, 1000);
        Order order2 = new Order(2L, 2000);

        Order.Receipt receipt1 = order1.new Receipt();
        Order.Receipt receipt2 = order2.new Receipt();

        assertEquals(1L, receipt1.getOrderId(), "order1에서 생성한 receipt는 1L의 id를 가져야합니다");
        assertEquals(900, receipt1.calculatePaymentAmount(100), "결과는 900원이어야 합니다.");

        assertEquals(2L, receipt2.getOrderId(), "order2에서 생성한 receipt는 2L의 id를 가져야합니다.");
        assertEquals(1800, receipt2.calculatePaymentAmount(200), "결과는 1800원이어야 합니다.");
    }

    @Test
    public void 지역_클래스가_주문메시지를_생성한다() {
        OrderMessageService backEnd = new OrderMessageService("백엔드몰");
        OrderMessageService javaMall = new OrderMessageService("자바몰");

        assertEquals("[주문] 백엔드몰 주문번호: 1", backEnd.createMessage(1L));
        assertEquals("[주문] 자바몰 주문번호: 20", javaMall.createMessage(20L));

    }

}
