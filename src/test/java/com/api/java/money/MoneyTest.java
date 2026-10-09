package com.api.java.money;

import com.api.java.money.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoneyTest {

    @DisplayName("10000원에 5000원을 더하면 새로운 객체는 15000원인지 테스트")
    @Test
    public void addMoneyTest() {

        Money money = new Money(10000);
        Money result = money.add(5000);

        //가격이 15000원이 되는지 테스트
        assertEquals(15000, result.getAmount(), "새로운 객체는 15000원이어야 합니다.");

        //금액을 추가해도 기존 객체는 10000원인지 테스트
        assertEquals(10000, money.getAmount(), "금액을 추가해도 기존 객체는 10000원이어야 합니다.");

        //5000원을 추가했을 때 객체가 새로 생성되는지 테스트
        assertNotSame(money, result, "금액을 추가하면 새로운 객체가 생성되어야 합니다.");
    }

    @DisplayName("금액이 같은 두 객체는 equals()가 true이고 hash값이 같은지 테스트")
    @Test
    public void equalsHashCodeTest(){
        Money money1 = new Money(10000);
        Money money2 = new Money(10000);

        assertEquals(money1, money2, "금액이 같은 두 객체는 equals결과가 true여야 합니다.");

        assertEquals(money1.hashCode(), money2.hashCode(), "금액이 같은 두 객체는 hashcode결과가 같아야 합니다.");
    }
}
