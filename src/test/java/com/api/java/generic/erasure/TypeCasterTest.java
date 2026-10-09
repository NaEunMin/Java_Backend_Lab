package com.api.java.generic.erasure;

import com.api.java.generic.erasure.TypeCaster;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TypeCasterTest {
    private final TypeCaster typeCaster = new TypeCaster();

    @Test
    public void 문자열객체를_String_class로_비교한다() {
        Object value = "Java";

        String result = typeCaster.cast(value, String.class);

        assertSame(value, result);
    }

    @Test
    public void Integer객체를_Number_class로_비교한다() {
        Object value = 1;

        Number result = typeCaster.cast(value, Number.class);

        assertSame(value, result);
    }

    @Test
    public void value가_targetType을_사용할_수_없다면_예외를_반환한다() {
        Object value = "Java";

        assertThrows(ClassCastException.class, () ->
                typeCaster.cast(value, Integer.class));
    }

    @Test
    public void value가_null이면_null을_반환한다() {
        Object value = null;

        assertNull(typeCaster.cast(value, Object.class));
    }

    @Test
    public void value와_targetType이_null이면_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () ->
                typeCaster.cast(null, null));
    }


    @Test
    public void targetType이_null이면_예외를_반환한다() {
        Object value = "Java";

        assertThrows(IllegalArgumentException.class, () ->
                typeCaster.cast(value, null));
    }

    @Test
    public void 서로다른_제네릭타입의_ArrayList의_타입이_같은지_검사한다() {
        List<String> str = new ArrayList<>();
        List<Integer> integer = new ArrayList<>();

        assertEquals(str.getClass(), integer.getClass());
        assertEquals(ArrayList.class, str.getClass());
        assertEquals(ArrayList.class, integer.getClass());
    }

}
