package com.api.java.string;

import com.api.java.string.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class StringBuilderTest {

    private final ProductService productService = new ProductService();

    @DisplayName("StringBuilder로 만든 객체가 같은 객체인지 테스트합니다")
    @Test
    public void equalStringBuilderTest() {
        StringBuilder builder = new StringBuilder();
        StringBuilder returned = builder.append("hello");

        assertEquals("hello", builder.toString(), "toString()의 값은 hello여야합니다.");
        assertSame(builder, returned, "append()는 같은 StringBuilder 객체를 반환해야합니다");

    }

    @DisplayName("toString()으로 만든 문자열 객체를 테스트합니다")
    @Test
    public void toStringTest() {
        StringBuilder builder = new StringBuilder("hello");
        String snapShot = builder.toString();
        builder.append(" world");

        assertEquals("hello", snapShot);
        assertEquals("hello world", builder.toString());
    }


    @DisplayName("joinProducts 메서드를 테스트합니다")
    @ParameterizedTest
    @MethodSource("joinProductsCases")
    public void joinProductsTest(List<String> products, String expected) {

        String result = productService.joinProducts(products);
        assertEquals(expected, result);
    }

    static Stream<Arguments> joinProductsCases() {
        return Stream.of(
                Arguments.of(List.of(),""),
                Arguments.of(List.of("상품1"), "상품1"),
                Arguments.of(List.of("상품1", "상품2"), "상품1, 상품2"),
                Arguments.of(List.of("상품1", "상품2", "상품3"), "상품1, 상품2, 상품3")
        );
    }
}
