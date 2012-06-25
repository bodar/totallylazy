package com.googlecode.totallylazy;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CountLatchTest {
    @Test
    public void supportsIncrementingAndDecrementing() throws Exception {
        final AtomicInteger number = new AtomicInteger();
        final CountLatch latch = new CountLatch();
        latch.countUp();
        assertThat(latch.count(), is(1));

        repeat(number).take(5).mapConcurrently(latch.monitor(new Function1<AtomicInteger, Integer>() {
            @Override
            public Integer call(AtomicInteger atomicInteger) throws Exception {
                Thread.sleep(10);
                return atomicInteger.incrementAndGet();
            }
        }));

        assertThat(number.get(), is(0));
        assertThat(latch.count(), is(6));

        latch.countDown();
        latch.await();

        assertThat(latch.count(), is(0));
        assertThat(number.get(), is(5));
    }
}
