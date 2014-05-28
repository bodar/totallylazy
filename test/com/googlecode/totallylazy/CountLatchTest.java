package com.googlecode.totallylazy;

import com.googlecode.totallylazy.concurrent.NamedExecutors;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static com.googlecode.totallylazy.PredicateAssert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Sequences.repeat;

public class CountLatchTest {
    @Test
    public void supportsIncrementingAndDecrementing() throws Exception {
        final AtomicInteger number = new AtomicInteger();
        final CountLatch latch = new CountLatch();

        latch.countUp();
        assertThat(latch.count(), is(1));

        ExecutorService executorService = NamedExecutors.newCachedThreadPool(getClass());
        for (final AtomicInteger atomicInteger : repeat(number).take(5)) {
            latch.countUp();

            executorService.submit(() -> {
                atomicInteger.incrementAndGet();
                latch.countDown();
            });
        }

        latch.countDown();
        latch.await();

        assertThat(latch.count(), is(0));
        assertThat(number.get(), is(5));
    }
}
