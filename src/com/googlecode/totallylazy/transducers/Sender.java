package com.googlecode.totallylazy.transducers;

import com.googlecode.totallylazy.Lists;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
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

public interface Sender<T> {

    AutoCloseable EMPTY_CLOSEABLE = () -> { };

    AutoCloseable send(Receiver<T> receiver);


    @SafeVarargs
    static <T> Sender<T> sender(T... values) {
        return sender(sequence(values));
    }

    static <T> Sender<T> sender(Iterable<? extends T> values) {
        return sender(values.iterator());
    }

    static <T> Sender<T> sender(Iterator<? extends T> iterator) {
        return observer -> {
            if (observer.start().equals(Stop)) return EMPTY_CLOSEABLE;
            while (iterator.hasNext()) {
                T value = iterator.next();
                if (observer.next(value).equals(Stop)) break;
            }
            observer.finish();
            return EMPTY_CLOSEABLE;
        };
    }

    default Sender<T> filter(Predicate<? super T> predicate) {
        return transduce(Transducers.filter(predicate));
    }

    default <R> Sender<R> map(Function1<? super T, ? extends R> mapper) {
        return transduce(Transducers.map(mapper));
    }

    default <R> Sender<R> flatMap(Function1<? super T, ? extends Sender<R>> mapper) {
        return transduce(Transducers.flatMap(mapper));
    }

    static <R> Sender<R> flatten(Sender<Sender<R>> nested) {
        return nested.flatMap(identity());
    }

    default <S> Sender<S> scan(S seed, Function2<? super S, ? super T, ? extends S> reducer) {
        return transduce(Transducers.scan(seed, reducer));
    }

    default <S> Sender<S> scan(Reducer<? super T, S> reducer) {
        return transduce(Transducers.scan(reducer));
    }

    default Sender<T> last() {
        return transduce(Transducers.last());
    }

    default <S> Sender<S> reduce(S seed, Function2<? super S, ? super T, ? extends S> reducer) {
        return transduce(Transducers.reduce(seed, reducer));
    }

    default <S> Sender<S> reduce(Reducer<? super T, S> reducer) {
        return transduce(Transducers.reduce(reducer));
    }

    default Sender<T> take(int limit) {
        return transduce(Transducers.take(limit));
    }

    default Sender<T> takeWhile(Predicate<? super T> predicate) {
        return transduce(Transducers.takeWhile(predicate));
    }

    default Sender<T> drop(int count) {
        return transduce(Transducers.drop(count));
    }

    default Sender<T> dropWhile(Predicate<? super T> predicate) {
        return filter(whileFalse(predicate));
    }

    default <K> Sender<Group<K, T>> groupBy(Function1<? super T, ? extends K> keyExtractor) {
        return transduce(Transducers.groupBy(keyExtractor));
    }

    default <R> Sender<R> transduce(Transducer<T, R> transducer) {
        return transduce(this, transducer);
    }

    static <T, R> Sender<R> transduce(Sender<T> sender, Transducer<T, R> transducer) {
        return observer -> sender.send(transducer.apply(observer));
    }

    default Sender<List<T>> toList() {
        return reduce(Lists.functions.add());
    }

    default Sender<Sequence<T>> toSequence() {
        return toList().map(Sequences::<T>sequence);
    }
}