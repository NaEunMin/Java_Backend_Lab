package java.api.java.generic.erasure;

import java.util.ArrayList;
import java.util.List;

public class TypeCaster {
    public <T> T cast(Object value, Class<T> targetType) {
        validTargetType(targetType);

        if (value == null) {
            return null;
        }
        if (!targetType.isInstance(value)) {
            throw new ClassCastException("value를 targetType으로 다룰 수 없습니다.");
        }
        return targetType.cast(value);
    }

    public <T> List<T> castAll(List<?> value, Class<T> targetType) {
        validArguments(value, targetType);

        List<T> result = new ArrayList<>();

        for (Object object : value) {
            result.add(cast(object, targetType)); //cast메서드 재사용
        }
        return result;
    }

    private void validTargetType(Class<?> targetType) {
        if (targetType == null) {
            throw new IllegalArgumentException("targetType은 null일 수 없습니다.");
        }
    }

    private void validArguments(Object value, Class<?> targetType) {
        if (value == null || targetType == null) {
            throw new IllegalArgumentException("value 또는 targetType은 null일 수 없습니다.");
        }
    }
}
