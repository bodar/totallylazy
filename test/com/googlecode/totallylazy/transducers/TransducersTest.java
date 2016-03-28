package com.googlecode.totallylazy.transducers;

import org.junit.Test;

import java.util.List;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.predicates.Predicates.instanceOf;
import static com.googlecode.totallylazy.predicates.Predicates.is;

public class TransducersTest {
    @Test
    public void canDecomposeTransducers() throws Exception {
        Transducer<Integer, List<Integer>> composite = Transducers.<Integer>identity().
                groupBy(i -> i % 2).
                flatMap(Sender::toList).
                last();

        List<Transducer<?, ?>> transducers = ((CompositeTransducer<?, ?, ?>) composite).transducers();
        assertThat(transducers.size(), is(4));
        assertThat(transducers.get(0), instanceOf(GroupByTransducer.class));
        assertThat(transducers.get(1), instanceOf(FlatMapTransducer.class));
        assertThat(transducers.get(2), instanceOf(LastOptionTransducer.class));
        assertThat(transducers.get(3), instanceOf(MapTransducer.class));
    }
}