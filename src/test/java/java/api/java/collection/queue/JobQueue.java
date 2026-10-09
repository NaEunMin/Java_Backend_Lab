package java.api.java.collection.queue;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

public class JobQueue<T> {
    private final Deque<T> jobs = new ArrayDeque<>();

    public void enqueue(T job) {
        if (job == null) {
            throw new IllegalArgumentException("작업은 null이 될 수 없습니다.");
        }
        jobs.offerLast(job);
    }

    public T dequeue() {
        T job = jobs.pollFirst();

        if (job == null) {
            throw new NoSuchElementException("큐가 비어있어 삭제할 데이터가 없습니다.");
        }
        return job;
    }

    public T peek() {
        T job = jobs.peekFirst();
        if (job == null) {
            throw new NoSuchElementException("큐가 비어있습니다.");
        }
        return job;
    }

    public void retry(T job) {
        if (job == null) {
            throw new IllegalArgumentException("작업은 null이 될 수 없습니다.");
        }
        jobs.offerFirst(job);
    }

    public int size() {
        return jobs.size();
    }

    public boolean isEmpty() {
        return jobs.isEmpty();
    }

}
