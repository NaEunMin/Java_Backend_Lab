package java.api.java.collection.list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListChunk {
    public <T> List<List<T>> chunk(
            List<? extends T> source,
            int chunkSize
    ) {
        if (source == null) {
            throw new IllegalArgumentException("source는 null일 수 없습니다.");
        }
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("chunkSize는 1이상이어야 합니다.");
        }

        List<List<T>> result = new ArrayList<>(); //null도 담아야하므로 ArrayList로 만들어야함.
        List<T> chunk = new ArrayList<>(); //현재 요소를 담을 리스트

        for (T element : source) {
            chunk.add(element);
            if (chunk.size() >= chunkSize) {
                result.add(chunk);
                chunk = new ArrayList<>(); //if문 밖에서 clear를 해버리면 기존의 chunk도 날라간다.
            }
        }

        if(!chunk.isEmpty()) {
            result.add(chunk);
        }

        return result;
    }

    public <T> List<List<T>> iteratorChunk(
            Iterable<? extends T> source,
            int chunkSize
    ) {
        if (source == null) {
            throw new IllegalArgumentException("source는 null일 수 없습니다.");
        }
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("chunkSize는 1이상이어야 합니다.");
        }

        List<List<T>> result = new ArrayList<>();
        List<T> chunk = new ArrayList<>();

        for (T element : source) {
            chunk.add(element);
            if (chunk.size() >= chunkSize) {
                result.add(chunk);
                chunk = new ArrayList<>();
            }
        }

        if(!chunk.isEmpty()) {
            result.add(chunk);
        }
        return result;
    }
}
