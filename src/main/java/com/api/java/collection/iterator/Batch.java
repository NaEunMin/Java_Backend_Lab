package com.api.java.collection.iterator;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Batch<T> implements Iterable<T> {
    private final List<T> items;

    public Batch(List<T> items) {
        //원본 리스트와 분리
        this.items = List.copyOf(items);
    }

    public int size() {
        return items.size();
    }

    @Override
    public Iterator<T> iterator() {
        return new BatchIterator();
    }

    private class BatchIterator implements Iterator<T> {

        private int currentIndex;

        @Override
        public boolean hasNext() {
            return currentIndex < items.size();
        }

        @Override
        public T next() {
            if(!hasNext()) {
                throw new NoSuchElementException("다음 값이 존재하지 않음");
            }
            return items.get(currentIndex++);
        }
    }
}
