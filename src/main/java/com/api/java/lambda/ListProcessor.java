package com.api.java.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ListProcessor {

    public <T> List<T> filter(
            List<T> source,
            Predicate<T> condition
    ) {
        if(source == null || condition == null){
            throw new IllegalArgumentException("source와 condition은 null일 수 없습니다.");
        }
        //새로운 리스트에 담기 위한 리스트 생성
        List<T> result = new ArrayList<>();
        for(T element : source){
            if(condition.test(element)){
                result.add(element);
            }
        }
        //반환 리스트는 외부에서 수정할 수 없다
        return List.copyOf(result);
    }

    public <T, R> List<R> map(
            List<T> source,
            Function<T,R> mapper
    ) {
        if(source == null || mapper == null){
            throw new IllegalArgumentException("source와 mapper는 null일 수 없습니다.");
        }

        List<R> result = new ArrayList<>();

        for(T element : source){
            R resultElement = mapper.apply(element);
            if(resultElement == null){
                throw new IllegalArgumentException("변환 결과는 null일 수 없습니다.");
            }
            result.add(resultElement);
        }

        return List.copyOf(result);
    }
}
