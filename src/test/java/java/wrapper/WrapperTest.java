package java.wrapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WrapperTest {

    @DisplayName("int의 박싱과 언박싱을 테스트합니다")
    @Test
    public void boxingTest(){
        int number1 = 10;
        Integer number2 = number1;
        int unboxed = number2;

        assertEquals(10, number2, "박싱을 했을 때 값은 같아야합니다.");
        assertEquals(10, unboxed,"언박싱을 했을 때 값은 같아야합니다.");
    }

    @DisplayName("문자열을 int, Integer로 변환합니다")
    @Test
    public void stringToIntTest(){
        int number1 = Integer.parseInt("10");
        Integer number2 = Integer.valueOf("10");

        assertEquals(10, number1);
        assertEquals(10, number2);
    }

    @DisplayName("잘못된 정수 포맷을 넣었을 때 예외가 발생하는지 테스트합니다.")
    @Test
    public void numberFormatExceptionTest(){
        assertThrows(NumberFormatException.class, () -> {
            Integer.parseInt("10a");
        });
    }

    @DisplayName("null을 언박싱할때 예외가 발생하는지 테스트합니다.")
    @Test
    public void nullUnboxingTest(){
        Integer number = null;
        assertThrows(NullPointerException.class, () -> {
            int number2 = number;
        });
    }

    @DisplayName("Integer의 값들이 같은지 테스트합니다")
    @Test
    public void integerValueEqualsTest(){
        Integer number1 = 1_000;
        Integer number2 = 1_000;

        assertEquals(number1, number2);
    }

    @DisplayName("Integer 캐싱이 되는지 테스트합니다")
    @Test
    public void integerCacheTest(){
        Integer number1 = 100;
        Integer number2 = 100;

        assertSame(number1, number2);
        assertEquals(number1, number2);
    }
}
