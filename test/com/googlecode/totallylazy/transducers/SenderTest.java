package com.googlecode.totallylazy.transducers;

import com.googlecode.totallylazy.predicates.Predicate;
import org.junit.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Lists.list;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.numbers.Numbers.average;
import static com.googlecode.totallylazy.numbers.Numbers.range;
import static com.googlecode.totallylazy.predicates.Predicates.instanceOf;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.predicates.Predicates.nullValue;
import static com.googlecode.totallylazy.predicates.Predicates.sameInstance;
import static com.googlecode.totallylazy.transducers.Sender.sender;
import static com.googlecode.totallylazy.transducers.Transducers.filter;

public class SenderTest {
    @Test
    public void canReceiveItems() throws Exception {
        assertReceived(sender(1, 2, 3), 1, 2, 3);
    }

    @Test
    public void supportsFiltering() throws Exception {
        assertReceived(sender(1, 2, 3, 4).filter(i -> i % 2 == 0), 2, 4);
    }

    @Test
    public void supportsFind() throws Exception {
        assertReceived(sender(1, 2, 3, 4).find(i -> i % 2 == 0), 2);
    }

    @Test
    public void supportsMapping() throws Exception {
        assertReceived(sender(1, 2, 3).map(Object::toString), "1", "2", "3");
    }

    @Test
    public void supportsFlatMapping() throws Exception {
        assertReceived(sender(1, 2, 3).flatMap((Integer i) -> sender(i, i * 2)), 1, 2, 2, 4, 3, 6);
    }

    @Test
    public void flatMappingTerminatesEarly() throws Exception {
        assertReceived(sender(range(1)).flatMap((Number i) -> sender(i, i.intValue() * 2)).take(6), 1, 2, 2, 4, 3, 6);
    }

    @Test
    public void supportsScan() throws Exception {
        assertReceived(sender(0, 2, 4).scan(average).map(Number::intValue), 0, 1, 2);
    }

    @Test
    public void supportsFirst() throws Exception {
        assertReceived(sender(0, 2, 4).first(), 0);
    }

    @Test
    public void firstThrowsErrorWhenNoElement() throws Exception {
        assertErrors(sender().first(), instanceOf(NoSuchElementException.class));
    }

    @Test
    public void supportsLast() throws Exception {
        assertReceived(sender(0, 2, 4).last(), 4);
    }

    @Test
    public void lastThrowsErrorWhenNoElement() throws Exception {
        assertErrors(sender().last(), instanceOf(NoSuchElementException.class));
    }

    @Test
    public void supportsReduce() throws Exception {
        assertReceived(sender(0, 2, 4).reduce(average).map(Number::intValue),
                2);
    }

    @Test
    public void supportsTake() throws Exception {
        assertReceived(sender(1, 2, 3, 4, 5, 6).take(0));
        assertReceived(sender(1, 2, 3, 4, 5, 6).take(1), 1);
        assertReceived(sender(1, 2, 3, 4, 5, 6).take(3), 1, 2, 3);
        assertReceived(sender(1, 2, 3, 4, 5, 6).take(6), 1, 2, 3, 4, 5, 6);
    }

    @Test
    public void takeTerminatesEarly() throws Exception {
        assertReceived(sender(repeat(() -> {
            throw new NoSuchElementException();
        })).take(0));
    }

    @Test
    public void supportsTakeWhile() throws Exception {
        assertReceived(sender(1, 2, 3, 4, 5, 6).takeWhile(i -> i < 4),
                1, 2, 3);
    }

    @Test
    public void supportsDrop() throws Exception {
        assertReceived(sender(1, 2, 3, 4, 5, 6).drop(3),
                4, 5, 6);
    }

    @Test
    public void supportsDropWhile() throws Exception {
        assertReceived(sender(1, 2, 3, 4, 5, 6).dropWhile(i -> i < 4),
                4, 5, 6);
    }

    @Test
    public void supportsGroupBy() throws Exception {
        Sender<List<Integer>> sender = sender(1, 2, 3, 4, 5, 6, 7, 8, 9).
                groupBy(i -> i % 2).
                flatMap(Sender::toList);

        assertReceived(sender,
                list(2, 4, 6, 8), list(1, 3, 5, 7, 9));
    }

    @Test
    public void supportsToList() throws Exception {
        assertReceived(sender(1, 2, 3, 4).toList(), list(1, 2, 3, 4));
    }

    @Test
    public void supportsToSequence() throws Exception {
        assertReceived(sender(1, 2, 3, 4).toSequence(), sequence(1, 2, 3, 4));
    }

    @Test
    public void supportsTransducers() throws Exception {
        assertReceived(sender(1, 2, 3, 4).transduce(filter((Integer x) -> x % 2 == 0).map(x -> x * 2)), 4, 8);
    }

    @SafeVarargs
    private final <T> void assertReceived(Sender<T> sender, T... values) {
        CapturingReceiver<T> receiver = new CapturingReceiver<>();
        sender.send(receiver);
        assertThat(receiver.items(), is(sequence(values)));
        assertThat(receiver.error(), nullValue());
        assertThat(receiver.started(), is(true));
        assertThat(receiver.finished(), is(true));
    }

    private final <T> void assertErrors(Sender<T> sender, Predicate<? super Throwable> throwable) {
        CapturingReceiver<T> receiver = new CapturingReceiver<>();
        sender.send(receiver);
        assertThat(receiver.items().isEmpty(), is(true));
        assertThat(receiver.error(), throwable);
        assertThat(receiver.started(), is(true));
        assertThat(receiver.finished(), is(true));
    }

    @Test
    public void canDecomposeTransducers() throws Exception {
        Sender<Integer> original = sender(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Sender<List<Integer>> composed = original.
                groupBy(i -> i % 2).
                flatMap(Sender::toList).
                last();

        CompositeSender<Integer,List<Integer>> compositeSender = cast(composed);
        assertThat(compositeSender.sender(), sameInstance(original));
        List<Transducer<?, ?>> transducers = compositeSender.transducers();
        assertThat(transducers.size(), is(4));
        assertThat(transducers.get(0), instanceOf(GroupByTransducer.class));
        assertThat(transducers.get(1), instanceOf(FlatMapTransducer.class));
        assertThat(transducers.get(2), instanceOf(LastOptionTransducer.class));
        assertThat(transducers.get(3), instanceOf(MapTransducer.class));
    }
}
