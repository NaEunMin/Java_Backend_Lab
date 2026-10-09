package com.api.java.reflection;

import com.api.java.member.Member;
import com.api.java.policy.GradePolicy;
import com.api.java.policy.StandardPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

public class ClassTest {

    @DisplayName("다양한 방법으로 얻은 클래스가 같은 클래스인지 테스트합니다.")
    @Test
    public void equalClassTest() throws ClassNotFoundException {

        //클래스 리터럴 방식
        Class<Member> literal = Member.class;

        Member member = new Member(1L, "테스트");

        //getClass 방식
        Class<?> runTime = member.getClass();

        //Class.forName 방식
        Class<?> dynamic = Class.forName("com.api.java.member.Member");

        assertSame(literal, runTime,"같은 클래스 로더에서 얻었다면 같은 클래스 객체를 참조합니다.");
        assertSame(literal, dynamic, "같은 클래스 로더에서 얻었다면 같은 클래스 객체를 참조합니다.");
    }

    @DisplayName("Class.forName 방식이 exception을 발생시키는지 테스트합니다.")
    @Test
    public void throwExceptionTest() {
        assertThrows(ClassNotFoundException.class, () -> {
            Class.forName("com.api.member.Members");
        });
    }

    @DisplayName("getName으로 클래스 이름과 멤버변수의 타입을 체크합니다.")
    @Test
    public void getNameAndTypeTest() throws NoSuchFieldException{
        Class<Member> memberClass = Member.class;

        String className = memberClass.getName();
        String simpleClassName = memberClass.getSimpleName();

        assertEquals("com.api.java.member.Member", className);
        assertEquals("Member", simpleClassName);

        Field field = Member.class.getDeclaredField("memberId");
        Class<?> type = field.getType();

        assertSame(long.class,type, "memberId는 long 타입입니다.");
    }

    @DisplayName("구현체의 타입을 체크합니다.")
    @Test
    public void implementTypeTest(){
        GradePolicy policy = new StandardPolicy();

        Class<?> classType = policy.getClass();

        assertSame(StandardPolicy.class, classType, "policy의 타입은 구현체의 타입이어야 합니다.");
        assertNotSame(GradePolicy.class, classType, "policy의 타입은 구현체의 타입이어야 합니다.");
    }

    @DisplayName("타입 대입 가능성을 체크합니다.")
    @Test
    public void typeAssignabilityTest(){
        Class<GradePolicy> targetType = GradePolicy.class;
        Class<StandardPolicy> sourceType = StandardPolicy.class;

        assertTrue(targetType.isAssignableFrom(sourceType));
        assertFalse(sourceType.isAssignableFrom(targetType));
    }

    @DisplayName("리플렉션으로 생성자를 찾아 객체를 생성합니다.")
    @Test
    public void reflectionConstructorTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<Member> constructor = Member.class.getDeclaredConstructor(long.class, String.class);

        Member member = constructor.newInstance(1L, "테스트");

        assertEquals(1L, member.getMemberId());
    }
}
