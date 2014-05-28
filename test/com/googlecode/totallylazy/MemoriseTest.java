package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.CountingCallable;
import com.googlecode.totallylazy.matchers.NumberMatcher;
import org.junit.Test;

import static com.googlecode.totallylazy.Callables.call;
import static com.googlecode.totallylazy.Callers.callConcurrently;
import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Runnables.doNothing;
import static com.googlecode.totallylazy.Sequences.memorise;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.callables.CountingCallable.counting;
import static com.googlecode.totallylazy.matchers.IterablePredicates.hasExactly;
import static java.util.Arrays.asList;
public class MemoriseTest {
    @Test
    public void supportsGetWithIndex() throws Exception {
        Sequence<Integer> counting = counting().repeat().memorise();
        assertThat(counting.get(0), is(0));
        assertThat(counting.get(0), is(0));
    }

    @Test
    public void canForget() throws Exception {
        CountingCallable<Integer> counting = counting();
        Sequence<Integer> memory = repeat(counting).memorise();
        assertThat(memory.head(), is(0));
        assertThat(counting.count(), is(1));
        
        ((Memory) memory).forget();
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
        final Sequence<Integer> number = repeat(counting.sleep(10)).memorise();

        Sequence<Integer> result = callConcurrently(callHead(number).sleep(10), callHead(number).sleep(10));

        assertThat(result.first(), is(0));
        assertThat(result.second(), is(0));
        assertThat(counting.count(), is(1));
    }

    private Returns<Integer> callHead(final Sequence<Integer> number) {
        return new Returns<Integer>() {
            public Integer call() throws Exception {
                return number.head();
            }
        };
    }

    @Test
    public void supportsMemorise() throws Exception {
        CountingCallable<Integer> counting = counting();
        Sequence<Integer> sequence = repeat(counting).memorise();
        assertThat(sequence.head(), is(0));
        assertThat(sequence.head(), is(0));
        assertThat(counting.count(), is(1));
    }
    
    @Test
    public void memorisingForEach() throws InterruptedException {
        CountingCallable<Integer> counting = counting();
        Sequence<Integer> sequence = sequence(counting).map(call(Integer.class)).memorise();
        sequence.forEach(doNothing(Integer.class));
        sequence.forEach(doNothing(Integer.class));

        assertThat(counting.count(), is(1));
    }

    @Test
    public void memorisingSize() throws InterruptedException {
        CountingCallable<Integer> counting = counting();
        Sequence<Integer> sequence = sequence(counting).map(call(Integer.class)).memorise();
        assertThat(sequence.size(), NumberMatcher.is(1));
        assertThat(counting.count(), is(1));
        assertThat(sequence.size(), NumberMatcher.is(1));
        assertThat(counting.count(), is(1));
    }
}
