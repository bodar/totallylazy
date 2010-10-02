package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.CountingCallable;
import com.googlecode.totallylazy.callables.SleepyCallable;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.*;

import static com.googlecode.totallylazy.Callables.call;
import static com.googlecode.totallylazy.Callables.increment;
import static com.googlecode.totallylazy.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.Sequences.memorise;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.callables.CountingCallable.counting;
import static com.googlecode.totallylazy.callables.SleepyCallable.sleepy;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MemoriseTest {
    @Test
    public void canTurnAnIteratorIntoAReUsableSequence() throws Exception {
        Sequence<Integer> reusable = memorise(asList(1, 2).iterator());
        assertThat(reusable, hasExactly(1, 2));
        assertThat(reusable, hasExactly(1, 2));
    }


    @Test
    public void memoriseIsThreadSafe() throws Exception {
        CountingCallable counting = counting();
        final Sequence<Integer> number = sequence(sleepy(counting, 10)).map(call(Integer.class)).memorise();

        Sequence<Integer> result = callConcurrently(50, callHead(number), callHead(number));

        assertThat(counting.getCount(), is(1));
        assertThat(result.first(), is(0));
        assertThat(result.second(), is(0));
    }

    public static <T> Sequence<T> callConcurrently(int timeout, Callable<T>... callables) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(callables.length);
        List<Callable<T>> collection = asList(callables);
        Sequence<Future<T>> result = sequence(service.invokeAll(collection));
        service.shutdown();
        service.awaitTermination(timeout, TimeUnit.MILLISECONDS);
        return result.map(Callables.<T>realise());
    }

    private Callable<Integer> callHead(final Sequence<Integer> number) {
        return new Callable<Integer>() {
            public Integer call() throws Exception {
                return number.head();
            }
        };
    }

    @Test
    public void supportsMemorise() throws Exception {
        CountingCallable counting = counting();
        Sequence<Integer> number = sequence(counting).map(call(Integer.class)).memorise();
        assertThat(number.head(), is(0));
        assertThat(number.head(), is(0));
        assertThat(counting.getCount(), is(1));
    }
    
    @Test
    public void memorisingForEach() throws InterruptedException {
        final int[] count = new int[]{0};
        Sequence<String> sequence = iterate(increment(), 0).take(1).map(recordNumberOfCalls(count)).memorise();
        sequence.forEach(doNothing());
        sequence.forEach(doNothing());

        assertThat(count[0], is(1));
    }

    @Test
    public void memorisingSize() throws InterruptedException {
        final int[] count = new int[]{0};
        Sequence<String> sequence = iterate(increment(), 0).take(1).map(recordNumberOfCalls(count)).memorise();
        sequence.size();
        sequence.size();

        assertThat(count[0], is(1));
    }

    private Runnable1<String> doNothing() {
        return new Runnable1<String>() {
            public void run(String aVoid) {
            }
        };
    }

    private Callable1<Integer, String> recordNumberOfCalls(final int[] count) {
        return new Callable1<Integer, String>() {
            public String call(Integer index) throws Exception {
                count[0]++;
                return "frangipan";
            }
        };
    }

}
