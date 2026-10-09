package java.string;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringTest {

    @DisplayName("String 객체들이 같은 객체인지 테스트합니다")
    @Test
    public void equalStringTest() {
        String str1 = "test1";
        String str2 = "test1";

        assertSame(str1, str2);
    }

    @DisplayName("new로 생성한 String 객체는 다른 객체인지 테스트합니다")
    @Test
    public void notEqualStringTest() {
        String str1 = "test1";
        String str2 = new String("test1");

        assertNotSame(str1, str2);
        assertEquals(str1, str2);
    }

    @DisplayName("컴파일타임에 만들어진 String 객체를 비교합니다")
    @Test
    public void compileTimeTest() {
        String hello = "hello";
        String compileTime = "he" + "llo";

        //컴파일타임에 만들어진 객체와 hello 비교
        assertEquals(hello, compileTime, "논리적으로 같은 값을 가져야합니다.");
        assertSame(hello, compileTime, "컴파일타임에 new로 String 객체를 만든 것이 아니라면 같은 객체여야합니다.");
    }

    @DisplayName("런타임에 만들어진 String 객체를 비교합니다")
    @Test
    public void runTimeTest(){
        String hello = "hello";

        String prefix = "he";
        String runTime = prefix + "llo";
        String pooled = runTime.intern();

        //런타임에 만들어진 객체와 hello 비교
        assertEquals(hello, runTime, "논리적으로 같은 값이어야 합니다.");
        assertNotSame(hello, runTime, "런타임에 만들어진 String 객체는 같은 객체가 아니어야합니다.");

        assertEquals(hello, pooled, "논리적으로 같은 값이어야 합니다.");
        assertSame(hello, pooled, "문자열 풀에 있다면 같은 객체여야합니다.");
        assertNotSame(pooled, runTime, "런타임에 만들어진 String 객체는 같은 객체가 아니어야합니다.");
    }

    @DisplayName("String 객체의 불변성을 테스트합니다.")
    @Test
    public void unmodifiedStringTest(){
        String hello = "hello";
        String changed = hello.concat(" world");

        assertEquals("hello", hello, "논리적으로 같은 값이어야 합니다.");
        assertEquals("hello world", changed, "논리적으로 같은 값이어야 합니다");
        assertNotSame(hello, changed, "concat은 다른 문자열 객체를 생성합니다.");

    }
}
