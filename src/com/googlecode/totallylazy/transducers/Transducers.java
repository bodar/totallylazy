package com.googlecode.totallylazy.transducers;

import com.googlecode.totallylazy.Lists;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.functions.Function2;
import com.googlecode.totallylazy.functions.Reducer;
import com.googlecode.totallylazy.predicates.Predicate;

import java.util.List;

public interface Transducers {
    static <A> Transducer<A, A> identity() { return new IdentityTransducer<>(); }
    static <A> Transducer<A, A> identity(Class<A> aClass) { return identity(); }

    static <A, B, C> Transducer<A, C> compose(Transducer<A, B> a, Transducer<B, C> b) {
        return CompositeTransducer.compositeTransducer(a, b);
    }

    static <A, B> Transducer<A, B> map(Function1<? super A, ? extends B> mapper) {
        return MapTransducer.mapTransducer(mapper);
    }

    static <A, B> Transducer<A, B> flatMap(Function1<? super A, ? extends Sender<B>> mapper) {
        return FlatMapTransducer.flatMapTransducer(mapper);
    }

    static <A> Transducer<A, A> filter(Predicate<? super A> predicate) {
        return FilterTransducer.filterTransducer(predicate);
    }

    static <A> Transducer<A, A> find(Predicate<? super A> predicate) {
        return compose(filter(predicate), first());
    }

    static <A, B> Transducer<A, B> scan(B seed, Function2<? super B, ? super A, ? extends B> reducer) {
        return ScanTransducer.scanTransducer(seed, reducer);
    }

    static <A, B> Transducer<A, B> scan(Reducer<? super A, B> reducer) {
        return ScanTransducer.scanTransducer(reducer);
    }

    static <A, B> Transducer<A, B> reduce(B seed, Function2<? super B, ? super A, ? extends B> reducer) {
        return compose(scan(seed, reducer), last());
    }

    static <A, B> Transducer<A, B> reduce(Reducer<? super A, B> reducer) {
        return compose(scan(reducer), last());
    }

    static <T> Transducer<T, T> first() {
        return FirstTransducer.firstTransducer();
    }

    static <T> Transducer<T, T> last() {
        return LastTransducer.lastTransducer();
    }

    static <A> Transducer<A, A> take(int limit) {
        return TakeTransducer.takeTransducer(limit);
    }

    static <A> Transducer<A, A> takeWhile(Predicate<? super A> predicate) {
        return TakeWhileTransducer.takeWhileTransducer(predicate);
    }

    static <A> Transducer<A, A> drop(int limit) {
        return DropTransducer.dropTransducer(limit);
    }

    static <A> Transducer<A, A> dropWhile(Predicate<? super A> predicate) {
        return DropWhileTransducer.dropWhileTransducer(predicate);
    }

    static <T, K> Transducer<T,Group<K, T>> groupBy(Function1<? super T, ? extends K> keyExtractor) {
        return GroupByTransducer.groupByTransducer(keyExtractor);
    }

    static <T> Transducer<T, List<T>> toList() {
        return reduce(Lists.functions.add());
    }

    static <T> Transducer<T, Sequence<T>> toSequence() {
        return compose(toList(), map(Sequences::<T>sequence));
    }

}
