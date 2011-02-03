package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.CountingCallable;
import org.junit.Test;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callables.call;
import static com.googlecode.totallylazy.Callers.callConcurrently;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.Sequences.memorise;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.callables.CountingCallable.counting;
import static com.googlecode.totallylazy.callables.SleepyCallable.sleepy;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
public class MemoriseTest {
    @Test
    public void canForget() throws Exception {
        CountingCallable<Integer> counting = counting();
        MemorisedSequence<Integer> memory = sequence(counting).map(call(Integer.class)).memorise();
        assertThat(memory.head(), is(0));
        assertThat(counting.count(), is(1));
        
        memory.forget();
        assertThat(memory.head(), is(1));
        assertThat(memory.head(), is(1));
        assertThat(counting.count(), is(2));
    }

    @Test
    public void canTurnAnIteratorIntoAReUsableSequence() throws Exception {
        Sequence<Integer> reusable = memorise(asList(1, 2).iterator());
        assertThat(reusable, hasExactly(1, 2));
        assertThat(reusable, hasExactly(1, 2));
    }

    @Test
    public void memoriseIsThreadSafe() throws Exception {
        CountingCallable<Integer> counting = counting();
        final Sequence<Integer> number = sequence(sleepy(counting, 10)).map(call(Integer.class)).memorise();

        Sequence<Integer> result = callConcurrently(callHead(number), callHead(number));

        assertThat(counting.count(), is(1));
        assertThat(result.first(), is(0));
        assertThat(result.second(), is(0));
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
        CountingCallable<Integer> counting = counting();
        Sequence<Integer> sequence = sequence(counting).map(call(Integer.class)).memorise();
        assertThat(sequence.head(), is(0));
        assertThat(sequence.head(), is(0));
        assertThat(counting.count(), is(1));
    }
    
    @Test
    public void memorisingForEach() throws InterruptedException {
        CountingCallable<Integer> counting = counting();
        Sequence<Integer> sequence = sequence(counting).map(call(Integer.class)).memorise();
        sequence.forEach(this.<Integer>doNothing());
        sequence.forEach(this.<Integer>doNothing());

        assertThat(counting.count(), is(1));
    }

    @Test
    public void memorisingSize() throws InterruptedException {
        CountingCallable<Integer> counting = counting();
        Sequence<Integer> sequence = sequence(counting).map(call(Integer.class)).memorise();
        sequence.size();
        sequence.size();

        assertThat(counting.count(), is(1));
    }

    private <T> Runnable1<T> doNothing() {
        return new Runnable1<T>() {
            public void run(T ignore) {
            }
        };
    }
}
