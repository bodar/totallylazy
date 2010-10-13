package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.callables.TimeReporter;
import com.googlecode.totallylazy.predicates.NumberMatcher;
import org.hamcrest.Matchers;
import org.junit.Test;

import static com.googlecode.totallylazy.Callables.add;
import static com.googlecode.totallylazy.Callables.increment;
import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.callables.TimeCallable.time;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

public class NumbersTest {
    @Test
    public void shouldBePrettyFast() throws Exception {
        TimeReporter run = new TimeReporter();
        final Number result = time(iterate(increment(), 0).take(10000), sum(), run);

        assertThat(run.time(), Matchers.is(lessThan(100d)));
        assertThat(result, NumberMatcher.is(49995000));
    }

    private Callable1<Sequence<Number>, Number> sum() {
        return new Callable1<Sequence<Number>, Number>() {
            public Number call(Sequence<Number> numberSequence) throws Exception {
                return numberSequence.reduceLeft(add());
            }
        };
    }

}
