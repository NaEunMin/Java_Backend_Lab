package com.api.java.generic.erasure;

import com.api.java.generic.erasure.TypeCaster;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ListTypeCasterTest {
    private final TypeCaster typeCaster = new TypeCaster();

    @Test
    public void 문자열리스트를_String_class로_비교한다() {
        List<String> value = new ArrayList<>(List.of("Java", "Jpa", "Spring"));

        List<String> result = typeCaster.castAll(value, String.class);

        assertNotSame(value, result); //새로운 리스트임을 확인
        assertEquals(value, result); //반환된 요소들이 같은지 확인
        for (int i = 0; i < value.size(); i++) {
            assertSame(value.get(i), result.get(i));
        }
    }

    @Test
    public void Integer리스트를_Number_class로_비교한다() {
        List<Integer> value = new ArrayList<>(List.of(1, 2, 3));

        List<Number> result = typeCaster.castAll(value, Number.class);

        assertEquals(value, result);
    }

    @Test
    public void value에서_하나라도_타입이_맞지않으면_예외를_반환한다() {
        List<?> value = new ArrayList<>(List.of(3, "java"));

        assertThrows(ClassCastException.class, () ->
                typeCaster.castAll(value, Integer.class));
    }

    @Test
    public void 빈_목록을_targetType으로_검사한다() {
        assertTrue(typeCaster.castAll(List.of(), String.class).isEmpty());
    }

    @Test
    public void value가_비어있고_targetType이_null이면_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () ->
                typeCaster.castAll(List.of(), null));
    }

    @Test
    public void targetTYpe과_value가_null이면_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () ->
                typeCaster.castAll(null, null));
    }

    @Test
    public void targetType이_null_이면_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () ->
                typeCaster.castAll(List.of(1, 2, 3), null));
    }

    @Test
    public void value가_null이면_예외을_반환한다() {
        List<?> value = null;

        assertThrows(IllegalArgumentException.class, () ->
                typeCaster.castAll(value, Object.class));
    }

    @Test
    public void value의_요소가_null이면_null을_반환한다() {
        List<?> value = new ArrayList<>();
        value.add(null);

        List<?> result = typeCaster.castAll(value, Object.class);

        assertEquals(value, result);
    }

    @Test
    public void 원본value는_변경하지_않는다() {
        List<Integer> value = new ArrayList<>(List.of(1, 2, 3));

        typeCaster.castAll(value, Number.class);

        assertEquals(List.of(1, 2, 3), value);
    }

}
