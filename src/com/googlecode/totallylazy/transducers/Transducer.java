package com.googlecode.totallylazy.transducers;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.functions.Function2;
import com.googlecode.totallylazy.functions.Reducer;
import com.googlecode.totallylazy.predicates.Predicate;

import java.util.List;

public interface Transducer<A, B> {
    Receiver<A> apply(Receiver<B> receiver);

    default <C> Transducer<A, C> compose(Transducer<B, C> b) {
        return Transducers.compose(this, b);
    }

    default Transducer<A, B> filter(Predicate<? super B> predicate) {
        return compose(Transducers.filter(predicate));
    }

    default Transducer<A, B> find(Predicate<? super B> predicate) {
        return compose(Transducers.find(predicate));
    }

    default <C> Transducer<A, C> map(Function1<? super B, ? extends C> mapper) {
        return compose(Transducers.map(mapper));
    }

    default <C> Transducer<A, C> flatMap(Function1<? super B, ? extends Sender<C>> mapper) {
        return compose(Transducers.flatMap(mapper));
    }

    default <C> Transducer<A, C> scan(C seed, Function2<? super C, ? super B, ? extends C> reducer) {
        return compose(Transducers.scan(seed, reducer));
    }

    default <C> Transducer<A, C> scan(Reducer<? super B, C> reducer) {
        return compose(Transducers.scan(reducer));
    }

    default <C> Transducer<A, C> reduce(C seed, Function2<? super C, ? super B, ? extends C> reducer) {
        return compose(Transducers.reduce(seed, reducer));
    }

    default <C> Transducer<A, C> reduce(Reducer<? super B, C> reducer) {
        return compose(Transducers.reduce(reducer));
    }

    default Transducer<A, B> first() {
        return compose(Transducers.first());
    }

    default Transducer<A, Option<B>> firstOption() {
        return compose(Transducers.firstOption());
    }

    default Transducer<A, B> last() {
        return compose(Transducers.last());
    }

    default Transducer<A, Option<B>> lastOption() {
        return compose(Transducers.lastOption());
    }

    default Transducer<A, B> take(int limit) {
        return compose(Transducers.take(limit));
    }

    default Transducer<A, B> takeWhile(Predicate<? super B> predicate) {
        return compose(Transducers.takeWhile(predicate));
    }

    default Transducer<A, B> drop(int limit) {
        return compose(Transducers.drop(limit));
    }

    default Transducer<A, B> dropWhile(Predicate<? super B> predicate) {
        return compose(Transducers.dropWhile(predicate));
    }

    default <K> Transducer<A, Group<K, B>> groupBy(Function1<? super B, ? extends K> keyExtractor) {
        return compose(Transducers.groupBy(keyExtractor));
    }

    default Transducer<A, List<B>> toList() {
        return compose(Transducers.toList());
    }

    default Transducer<A, Sequence<B>> toSequence() {
        return compose(Transducers.toSequence());
    }
}

