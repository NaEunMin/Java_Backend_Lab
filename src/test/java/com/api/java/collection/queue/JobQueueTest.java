package com.api.java.collection.queue;

import com.api.java.collection.queue.JobQueue;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class JobQueueTest {
    private final JobQueue<String> jobQueue = new JobQueue<>();

    @Test
    public void 작업은_추가된_순서대로_실행된다() {
        jobQueue.enqueue("job1");
        jobQueue.enqueue("job2");
        jobQueue.enqueue("job3");

        assertEquals("job1", jobQueue.dequeue());
        assertEquals("job2", jobQueue.dequeue());
        assertEquals("job3", jobQueue.dequeue());
    }

    @Test
    public void dequeue로_처리된_작업은_대기열에서_제거된다() {
        jobQueue.enqueue("job1");
        jobQueue.dequeue();

        assertTrue(jobQueue.isEmpty());
    }

    @Test
    public void peek로_처리된_작업은_대기열에서_제거되지_않는다() {
        jobQueue.enqueue("job1");

        assertEquals("job1", jobQueue.peek());
        assertEquals(1, jobQueue.size());
    }

    @Test
    public void 빈_대기열에서_dequeue를_하면_예외를_발생시킨다() {
        assertThrows(NoSuchElementException.class, jobQueue::dequeue);
    }

    @Test
    public void 빈_대기열에서_peek를_하면_예외를_발생시킨다() {
        assertThrows(NoSuchElementException.class, jobQueue::peek);
    }

    @Test
    public void 재시도_작업은_기존_작업보다_먼저_처리된다() {
        jobQueue.enqueue("job2");
        jobQueue.enqueue("job3");
        jobQueue.retry("job1");

        assertEquals("job1", jobQueue.dequeue());
        assertEquals("job2", jobQueue.dequeue());
    }

    @Test
    public void null_작업을_추가하면_예외를_발생시킨다() {
        assertThrows(IllegalArgumentException.class, () ->
                jobQueue.enqueue(null));
    }

    @Test
    public void size와isEmpty는_현재상태를_정확히_반환한다(){
        jobQueue.enqueue("job1");
        jobQueue.enqueue("job2");

        assertEquals(2, jobQueue.size());
        assertFalse(jobQueue.isEmpty());

        jobQueue.dequeue();
        jobQueue.dequeue();

        assertEquals(0, jobQueue.size());
        assertTrue(jobQueue.isEmpty());
    }
}
