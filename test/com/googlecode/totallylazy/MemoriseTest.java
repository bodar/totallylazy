package com.googlecode.totallylazy;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.*;

import static com.googlecode.totallylazy.Callables.call;
import static com.googlecode.totallylazy.Callables.increment;
import static com.googlecode.totallylazy.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.Sequences.memorise;
import static com.googlecode.totallylazy.Sequences.sequence;
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
        final int[] count = {0};
        final Sequence<Integer> number = sequence(new Callable<Integer>() {
            public Integer call() throws Exception {
                int current = count[0]++;
                Thread.sleep(10);
                return current;
            }
        }).map(call(Integer.class)).memorise();

        ExecutorService service = Executors.newFixedThreadPool(2);

        List<Callable<Integer>> collection = asList(callHead(number), callHead(number));
        List<Future<Integer>> result = service.invokeAll(collection);
        service.shutdown();
        service.awaitTermination(50, TimeUnit.MILLISECONDS);

        assertThat(count[0], is(1));
        assertThat(result.get(0).get(), is(0));
        assertThat(result.get(1).get(), is(0));
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
        final int[] count = {0};
        Sequence<Integer> number = sequence(new Callable<Integer>() {
            public Integer call() throws Exception {
                return count[0]++;
            }
        }).map(call(Integer.class)).memorise();
        assertThat(number.head(), is(0));
        assertThat(number.head(), is(0));
        assertThat(count[0], is(1));
    }
    
    @Test
    public void memorisingForEach() throws InterruptedException {
        final int[] count = new int[]{0};
        Sequence<String> sequence = iterate(increment(), 0).take(1).map(recordNumberOfCalls(count)).memorise();
        sequence.foreach(doNothing());
        sequence.foreach(doNothing());

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
