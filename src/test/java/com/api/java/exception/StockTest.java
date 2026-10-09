package com.api.java.exception;

import com.api.java.exception.InsufficientStockException;
import com.api.java.exception.Stock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class StockTest {

    @Test
    public void 요청수량만큼_현재재고를_감소시킨다() {
        Stock stock1 = new Stock(10);
        Stock stock2 = new Stock(5);

        stock1.decrease(3);
        stock2.decrease(5);
        assertEquals(7, stock1.getQuantity());
        assertEquals(0, stock2.getQuantity());
    }

    @Test
    public void 요청수량이_현재재고보다_크면_예외를_발생시킨다() {
        Stock stock = new Stock(5);
        InsufficientStockException e =
                assertThrows(InsufficientStockException.class, () -> {
                    stock.decrease(6);
                });
        assertEquals(5, e.getAvailableQuantity());
        assertEquals(6, e.getRequestedQuantity());
        assertEquals(5, stock.getQuantity(), "예외가 던져졌다면 핸재재고는 변하지 않아야합니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    public void 요청수량이_1보다_작다면_예외를_발생시킨다(int amount) {
        Stock stock = new Stock(5);
        assertThrows(IllegalArgumentException.class, () -> {
            stock.decrease(amount);
        });

        assertEquals(5, stock.getQuantity());
    }

    @Test
    public void 초기재고는_0일수있다(){
        Stock stock = new Stock(0);

        assertEquals(0, stock.getQuantity());
    }

    @Test
    public void 초기재고가_음수라면_예외를_발생시킨다(){
        assertThrows(IllegalArgumentException.class, () -> {
            Stock stock = new Stock(-1);
        });
    }
}
