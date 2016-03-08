package com.googlecode.totallylazy.transducers;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.functions.Function2;
import com.googlecode.totallylazy.functions.Reducer;
import com.googlecode.totallylazy.predicates.Predicate;

import java.util.Iterator;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.functions.Functions.identity;
import static com.googlecode.totallylazy.predicates.Predicates.whileFalse;
import static com.googlecode.totallylazy.transducers.State.Stop;

public interface Sender<A> {

    AutoCloseable EMPTY_CLOSEABLE = () -> {
    };

    AutoCloseable send(Receiver<A> receiver);


    @SafeVarargs
    static <A> Sender<A> sender(A... values) {
        return sender(sequence(values));
    }

    static <A> Sender<A> sender(Iterable<? extends A> values) {
        return sender(values.iterator());
    }

    static <A> Sender<A> sender(Iterator<? extends A> iterator) {
        return receiver -> {
            if (receiver.start().equals(Stop)) return EMPTY_CLOSEABLE;
            while (iterator.hasNext()) {
                A value = iterator.next();
                if (receiver.next(value).equals(Stop)) break;
            }
            receiver.finish();
            return EMPTY_CLOSEABLE;
        };
    }

    default Sender<A> filter(Predicate<? super A> predicate) {
        return transduce(Transducers.filter(predicate));
    }

    default <B> Sender<B> map(Function1<? super A, ? extends B> mapper) {
        return transduce(Transducers.map(mapper));
    }

    default <B> Sender<B> flatMap(Function1<? super A, ? extends Sender<B>> mapper) {
        return transduce(Transducers.flatMap(mapper));
    }

    static <B> Sender<B> flatten(Sender<Sender<B>> nested) {
        return nested.flatMap(identity());
    }

    default <B> Sender<B> scan(B seed, Function2<? super B, ? super A, ? extends B> reducer) {
        return transduce(Transducers.scan(seed, reducer));
    }

    default <B> Sender<B> scan(Reducer<? super A, B> reducer) {
        return transduce(Transducers.scan(reducer));
    }

    default Sender<A> last() {
        return transduce(Transducers.last());
    }

    default <B> Sender<B> reduce(B seed, Function2<? super B, ? super A, ? extends B> reducer) {
        return transduce(Transducers.reduce(seed, reducer));
    }

    default <B> Sender<B> reduce(Reducer<? super A, B> reducer) {
        return transduce(Transducers.reduce(reducer));
    }

    default Sender<A> take(int limit) {
        return transduce(Transducers.take(limit));
    }

    default Sender<A> takeWhile(Predicate<? super A> predicate) {
        return transduce(Transducers.takeWhile(predicate));
    }

    default Sender<A> drop(int count) {
        return transduce(Transducers.drop(count));
    }

    default Sender<A> dropWhile(Predicate<? super A> predicate) {
        return filter(whileFalse(predicate));
    }

    default <B> Sender<Group<B, A>> groupBy(Function1<? super A, ? extends B> keyExtractor) {
        return transduce(Transducers.groupBy(keyExtractor));
    }

    default <B> Sender<B> transduce(Transducer<A, B> transducer) {
        return transduce(this, transducer);
    }

    static <A, B> Sender<B> transduce(Sender<A> sender, Transducer<A, B> transducer) {
        return CompositeSender.compositeSender(sender, transducer);
    }

    default Sender<List<A>> toList() {
        return transduce(Transducers.toList());
    }

    default Sender<Sequence<A>> toSequence() {
        return transduce(Transducers.toSequence());
    }

}