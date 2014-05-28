package com.googlecode.totallylazy;

import com.googlecode.totallylazy.collections.AbstractCollection;

import java.util.Comparator;
import java.util.concurrent.Executor;

import static com.googlecode.totallylazy.Sequences.sequence;

public abstract class Sequence<T> extends AbstractCollection<T> implements Seq<T> {
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Iterable) return Sequences.equalTo(this, (Iterable<?>) obj);
        return obj instanceof Segment && Segment.methods.equalTo(this, (Segment<?>) obj);
    }

    public String toString() {
        return Sequences.toString(this);
    }

    @Override
    public Sequence<T> deleteAll(final Iterable<? extends T> iterable) {
        return Sequences.deleteAll(this, iterable);
    }

    @Override
    public Sequence<T> eachConcurrently(Function<? super T, ?> runnable) {
        return Sequences.eachConcurrently(this, runnable);
    }

    @Override
    public Sequence<T> forEachConcurrently(Function<? super T, ?> runnable) {
        return Sequences.forEachConcurrently(this, runnable);
    }

    @Override
    public Sequence<T> eachConcurrently(Function<? super T, ?> runnable, Executor executor) {
        return Sequences.eachConcurrently(this, runnable);
    }

    @Override
    public Sequence<T> forEachConcurrently(Function<? super T, ?> runnable, Executor executor) {
        return Sequences.forEachConcurrently(this, runnable);
    }

    @Override
    public Sequence<T> each(Block<? super T> runnable) {
        return Sequences.each(this, runnable);
    }

    @Override
    public Sequence<T> each(Function<? super T, ?> runnable) {
        return Sequences.each(this, runnable);
    }

    @Override
    public Sequence<T> forEach(Block<? super T> runnable) {
        return Sequences.forEach(this, runnable);
    }

    @Override
    public Sequence<T> forEach(Function<? super T, ?> runnable) {
        return Sequences.forEach(this, runnable);
    }

    @Override
    public <S> Sequence<S> mapConcurrently(Function<? super T, S> callable) {
        return Sequences.mapConcurrently(this, callable);
    }

    @Override
    public <S> Sequence<S> mapConcurrently(Function<? super T, S> callable, Executor executor) {
        return Sequences.mapConcurrently(this, callable, executor);
    }

    @Override
    public <S> Sequence<S> map(Function<? super T, ? extends S> callable) {
        return Sequences.map(this, callable);
    }

    @Override
    public Pair<Sequence<T>, Sequence<T>> partition(Predicate<? super T> predicate) {
        return Sequences.partition(this, predicate);
    }

    @Override
    public Sequence<T> filter(Predicate<? super T> predicate) {
        return Sequences.filter(this, predicate);
    }

    @Override
    public <S> Sequence<S> flatMap(Function<? super T, ? extends Iterable<? extends S>> callable) {
        return Sequences.flatMap(this, callable);
    }

    @Override
    public <S> Sequence<S> flatMapConcurrently(Function<? super T, ? extends Iterable<? extends S>> callable) {
        return Sequences.flatMapConcurrently(this, callable);
    }

    @Override
    public <S> Sequence<S> flatMapConcurrently(Function<? super T, ? extends Iterable<? extends S>> callable, Executor executor) {
        return Sequences.flatMapConcurrently(this, callable, executor);
    }

    @Override
    public <B> Sequence<B> applicate(Seq<? extends Function<? super T, ? extends B>> applicator) {
        return Sequences.applicate(applicator, this);
    }

    @Override
    public Sequence<T> tail() {
        return Sequences.tail(this);
    }

    @Override
    public Sequence<T> init() {
        return Sequences.init(this);
    }

    @Override
    public Sequence<T> unique() {
        return Sequences.unique(this);
    }

    @Override
    public <S> Sequence<T> unique(Function<? super T, ? extends S> callable) {
        return Sequences.unique(this, callable);
    }

    @Override
    public Sequence<T> empty() {
        return Sequences.empty();
    }

    @Override
    public Sequence<T> delete(T t) {
        return Sequences.delete(this, t);
    }

    @Override
    public Sequence<T> take(int count) {
        return Sequences.take(this, count);
    }

    @Override
    public Sequence<T> takeWhile(Predicate<? super T> predicate) {
        return Sequences.takeWhile(this, predicate);
    }

    @Override
    public Sequence<T> drop(int count) {
        return Sequences.drop(this, count);
    }

    @Override
    public Sequence<T> dropWhile(Predicate<? super T> predicate) {
        return Sequences.dropWhile(this, predicate);
    }

    @Override
    public Sequence<T> append(T t) {
        return Sequences.append(this, t);
    }

    @Override
    public Sequence<T> join(Iterable<? extends T> iterable) {
        return Sequences.join(this, iterable);
    }

    @Override
    public Sequence<T> cons(T t) {
        return Sequences.cons(t, this);
    }

    @Override
    public Sequence<T> memoize() {
        return Sequences.memorise(this);
    }

    @Override
    public Sequence<T> memorise() {
        return Sequences.memorise(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Sequence<Sequence<T>> transpose(Iterable<? extends T>... iterables) {
        return transpose(sequence(iterables));
    }

    @Override
    public Sequence<Sequence<T>> transpose(Iterable<? extends Iterable<? extends T>> iterables) {
        return Sequences.transpose(Sequences.cons(this, sequence(iterables).<Iterable<T>>unsafeCast()));
    }

    @Override
    public <S> Sequence<Pair<T, S>> zip(Iterable<? extends S> second) {
        return Sequences.zip(this, second);
    }

    @Override
    public <S, Th> Sequence<Triple<T, S, Th>> zip(Iterable<? extends S> second, Iterable<? extends Th> third) {
        return Sequences.zip(this, second, third);
    }

    @Override
    public <S, Th, Fo> Sequence<Quadruple<T, S, Th, Fo>> zip(Iterable<? extends S> second, Iterable<? extends Th> third, Iterable<? extends Fo> fourth) {
        return Sequences.zip(this, second, third, fourth);
    }

    @Override
    public <S, Th, Fo, Fi> Sequence<Quintuple<T, S, Th, Fo, Fi>> zip(Iterable<? extends S> second, Iterable<? extends Th> third, Iterable<? extends Fo> fourth, Iterable<? extends Fi> fifth) {
        return Sequences.zip(this, second, third, fourth, fifth);
    }

    @Override
    public Sequence<Pair<Number, T>> zipWithIndex() {
        return Sequences.zipWithIndex(this);
    }

    @Override
    public <R extends Comparable<? super R>> Sequence<T> sortBy(Function<? super T, ? extends R> callable) {
        return Sequences.sortBy(this, callable);
    }

    @Override
    public Sequence<T> sort(Comparator<? super T> comparator) {
        return Sequences.sortBy(this, comparator);
    }

    @Override
    public Sequence<T> sortBy(Comparator<? super T> comparator) {
        return Sequences.sortBy(this, comparator);
    }

    @Override
    public <S> Sequence<S> safeCast(Class<? extends S> aClass) {
        return Sequences.safeCast(this, aClass);
    }

    @Override
    public <S> Sequence<S> unsafeCast() {
        return Sequences.unsafeCast(this);
    }

    @Override
    public Sequence<T> realise() {
        return Sequences.realise(this);
    }

    @Override
    public Sequence<T> reverse() {
        return Sequences.reverse(this);
    }

    @Override
    public Sequence<T> cycle() {
        return Sequences.cycle(this);
    }

    @Override
    public <K> Sequence<Group<K, T>> groupBy(Function<? super T, ? extends K> callable) {
        return Sequences.groupBy(this, callable);
    }

    @Override
    public Sequence<Sequence<T>> recursive(Function<? super Sequence<T>, Pair<? extends Iterable<? extends T>, ? extends Iterable<? extends T>>> callable) {
        return Sequences.recursive(this, callable);
    }

    @Override
    public Pair<Sequence<T>, Sequence<T>> splitAt(int index) {
        return Sequences.splitAt(this, index);
    }

    @Override
    public Pair<Sequence<T>, Sequence<T>> splitWhen(Predicate<? super T> predicate) {
        return Sequences.splitWhen(this, predicate);
    }

    @Override
    public Pair<Sequence<T>, Sequence<T>> splitOn(T instance) {
        return Sequences.splitOn(this, instance);
    }

    @Override
    public Pair<Sequence<T>, Sequence<T>> span(Predicate<? super T> predicate) {
        return Sequences.span(this, predicate);
    }

    @Override
    public Pair<Sequence<T>, Sequence<T>> breakOn(Predicate<? super T> predicate) {
        return Sequences.breakOn(this, predicate);
    }

    @Override
    public Sequence<T> shuffle() {
        return Sequences.shuffle(this);
    }

    @Override
    public Sequence<T> interruptable() {
        return Sequences.interruptable(this);
    }

    @Override
    public Sequence<Pair<T, T>> cartesianProduct() {
        return Sequences.cartesianProduct(this);
    }

    @Override
    public <S> Sequence<Pair<T, S>> cartesianProduct(Iterable<? extends S> other) {
        return Sequences.cartesianProduct(this, other);
    }

    @Override
    public Sequence<Sequence<T>> windowed(int size) {
        return Sequences.windowed(this, size);
    }

    @Override
    public Sequence<T> intersperse(T separator) {
        return Sequences.intersperse(this, separator);
    }

    @Override
    public Option<Sequence<T>> flatOption() {
        return Sequences.flatOption(this);
    }

    @Override
    public Sequence<Sequence<T>> grouped(int size) {
        return Sequences.grouped(this, size);
    }
}
