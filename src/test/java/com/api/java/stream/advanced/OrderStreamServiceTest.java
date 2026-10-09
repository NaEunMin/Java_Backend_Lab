package com.api.java.stream.advanced;

import com.api.java.stream.advanced.OrderItem;
import com.api.java.stream.advanced.OrderStreamService;
import com.api.java.stream.advanced.PurchaseOrder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderStreamServiceTest {
    private final OrderStreamService orderStreamService = new OrderStreamService();

    @Test
    public void 여러_주문의_상품을_나열한다() {
        OrderItem orderItem1 = new OrderItem("키보드", 50_000, 1);
        OrderItem orderItem2 = new OrderItem("마우스", 20_000, 2);

        OrderItem orderItem3 = new OrderItem("마우스", 20_000, 1);
        OrderItem orderItem4 = new OrderItem("모니터", 300_000, 2);

        PurchaseOrder order1 = new PurchaseOrder(1L, List.of(orderItem1, orderItem2));
        PurchaseOrder order2 = new PurchaseOrder(2L, List.of(orderItem3, orderItem4));

        List<String> result = orderStreamService.findDistinctProductNames(List.of(order1, order2));

        assertEquals(List.of("마우스", "모니터", "키보드"), result);
    }

    @Test
    public void 중복_상품명을_한_번만_반환한다() {
        OrderItem item1 = new OrderItem("키보드", 50_000, 1);
        OrderItem item2 = new OrderItem("키보드", 40_000, 2);

        PurchaseOrder order1 = new PurchaseOrder(1L, List.of(item1, item2));

        List<String> result = orderStreamService.findDistinctProductNames(List.of(order1));

        assertEquals(List.of("키보드"), result);
    }

    @Test
    public void 상품명을_오름차순으로_반환한다() {
        OrderItem item1 = new OrderItem("바나나", 1_000, 1);
        OrderItem item2 = new OrderItem("다리미", 30_000, 2);
        OrderItem item3 = new OrderItem("가위", 3_000, 3);

        PurchaseOrder order1 = new PurchaseOrder(1L, List.of(item1, item2, item3));

        List<String> result = orderStreamService.findDistinctProductNames(List.of(order1));

        assertEquals(List.of("가위", "다리미", "바나나"), result);
    }

    @Test
    public void 전체_매출을_계산한다() {
        OrderItem item1 = new OrderItem("키보드", 50_000, 1);
        OrderItem item2 = new OrderItem("키보드", 40_000, 2);

        PurchaseOrder order1 = new PurchaseOrder(1L, List.of(item1, item2));

        long result = orderStreamService.calculateTotalSales(List.of(order1));

        assertEquals(130_000, result);
    }

    @Test
    public void 빈_상품_주문의_매출은_0이다() {
        PurchaseOrder order1 = new PurchaseOrder(1L, List.of());

        long result = orderStreamService.calculateTotalSales(List.of(order1));

        assertEquals(0L, result);
    }

    @Test
    public void 빈_주문의_매출은_0이다() {
        long result = orderStreamService.calculateTotalSales(List.of());

        assertEquals(0L, result);
    }

    @Test
    public void 기준_가격_이상의_상품이있다면_true이다() {
        OrderItem item1 = new OrderItem("키보드", 50_000, 1);
        OrderItem item2 = new OrderItem("마우스", 20_000, 2);

        OrderItem item3 = new OrderItem("마우스", 20_000, 1);
        OrderItem item4 = new OrderItem("모니터", 300_000, 2);

        PurchaseOrder order1 = new PurchaseOrder(1L, List.of(item1, item2, item3, item4));

        assertTrue(orderStreamService.containsExpensiveItem(List.of(order1), 300_000));
    }

    @Test
    public void 기준_가격_이상의_상품이_없다면_false이다() {
        OrderItem item1 = new OrderItem("키보드", 50_000, 1);
        OrderItem item2 = new OrderItem("마우스", 20_000, 2);

        OrderItem item3 = new OrderItem("마우스", 20_000, 1);
        OrderItem item4 = new OrderItem("모니터", 300_000, 2);

        PurchaseOrder order1 = new PurchaseOrder(1L, List.of(item1, item2, item3, item4));

        assertFalse(orderStreamService.containsExpensiveItem(List.of(order1), 300_001));
    }

    @Test
    public void 모든_주문에_상품이_있다면_true이다() {
        OrderItem item1 = new OrderItem("키보드", 50_000, 1);
        OrderItem item2 = new OrderItem("마우스", 20_000, 2);

        OrderItem item3 = new OrderItem("마우스", 20_000, 1);
        OrderItem item4 = new OrderItem("모니터", 300_000, 2);

        PurchaseOrder order1 = new PurchaseOrder(1L, List.of(item1, item2));
        PurchaseOrder order2 = new PurchaseOrder(2L, List.of(item3, item4));

        assertTrue(orderStreamService.allOrdersHaveItems(List.of(order1, order2)));
    }

    @Test
    public void 하나라도_빈_주문이_있다면_false이다() {
        OrderItem item1 = new OrderItem("키보드", 50_000, 1);
        OrderItem item2 = new OrderItem("마우스", 20_000, 2);

        PurchaseOrder order1 = new PurchaseOrder(1L, List.of(item1, item2));
        PurchaseOrder order2 = new PurchaseOrder(2L, List.of());

        assertFalse(orderStreamService.allOrdersHaveItems(List.of(order1, order2)));
    }

    @Test
    public void 빈_주문_목록에서_allMatch는_true를_반환한다() {
        assertTrue(orderStreamService.allOrdersHaveItems(List.of()));
    }

    @Test
    public void null주문은_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () ->
                orderStreamService.findDistinctProductNames(null));
    }

    @Test
    public void 음수_기준_가격은_예외를_반환한다() {
        OrderItem item1 = new OrderItem("키보드", 50_000, 1);
        OrderItem item2 = new OrderItem("마우스", 20_000, 2);

        PurchaseOrder order1 = new PurchaseOrder(1L, List.of(item1, item2));

        assertThrows(IllegalArgumentException.class, () ->
                orderStreamService.containsExpensiveItem(List.of(order1), -1));
    }

    @Test
    public void 반환된_상품_리스트는_수정할_수_없다() {
        OrderItem item1 = new OrderItem("키보드", 50_000, 1);
        OrderItem item2 = new OrderItem("마우스", 20_000, 2);

        PurchaseOrder order1 = new PurchaseOrder(1L, List.of(item1, item2));

        List<String> result = orderStreamService.findDistinctProductNames(List.of(order1));

        assertThrows(UnsupportedOperationException.class, () ->
                result.add("모니터"));
    }
}
