package com.googlecode.totallylazy.transducers;

import org.junit.Test;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Lists.list;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.numbers.Numbers.average;
import static com.googlecode.totallylazy.numbers.Numbers.range;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.predicates.Predicates.nullValue;
import static com.googlecode.totallylazy.transducers.Sender.sender;
import static com.googlecode.totallylazy.transducers.Sender.transduce;
import static com.googlecode.totallylazy.transducers.Transducers.filter;

public class SenderTest {
    @Test
    public void canObserverItems() throws Exception {
        assertReceived(sender(1, 2, 3), 1, 2, 3);
    }

    @Test
    public void supportsFiltering() throws Exception {
        assertReceived(sender(1, 2, 3).filter(i -> i % 2 == 0), 2);
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
    public void supportsLast() throws Exception {
        assertReceived(sender(0, 2, 4).last(), 4);
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
        assertReceived(sender(1, 2, 3, 4, 5, 6, 7, 8, 9).
                        groupBy(i -> i % 2).
                        flatMap(Sender::toList),
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
        assertReceived(transduce(sender(1, 2, 3, 4), filter((Integer x) -> x % 2 == 0).map(x -> x * 2)), 4, 8);
    }

    @SafeVarargs
    private final <T> void assertReceived(Sender<T> sender, T... values) {
        CapturingReceiver<T> observer = new CapturingReceiver<>();
        sender.send(observer);
        assertThat(observer.items(), is(sequence(values)));
        assertThat(observer.error(), nullValue());
        assertThat(observer.started(), is(true));
        assertThat(observer.finished(), is(true));
    }
}
