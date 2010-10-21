package com.googlecode.totallylazy;

import com.googlecode.totallylazy.proxy.Generics;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.List;
import java.util.Set;


public abstract class Sequence<T> implements Iterable<T>, First<T>, Second<T> {
    public final void forEach(final Runnable1<T> runnable) {
        Sequences.forEach(this, runnable);
    }

    public final <S> Sequence<S> map(final Callable1<? super T, S> callable) {
        return Sequences.map(this, callable);
    }

    public final Sequence<T> filter(final Predicate<? super T> predicate) {
        return Sequences.filter(this, predicate);
    }

    public final <S> Sequence<S> flatMap(final Callable1<? super T, Iterable<S>> callable) {
        return Sequences.flatMap(this, callable);
    }

    public final T first() {
        return Sequences.first(this);
    }

    public final T second() {
        return Sequences.second(this);
    }

    public final T head() {
        return Sequences.head(this);
    }

    public final Option<T> headOption() {
        return Sequences.headOption(this);
    }

    public final Sequence<T> tail() {
        return Sequences.tail(this);
    }

    public final <S> S foldLeft(final S seed, final Callable2<? super S, ? super T, S> callable) {
        return Sequences.foldLeft(this, seed, callable);
    }

    public final T reduceLeft(final Callable2<? super T, ? super T, T> callable) {
        return Sequences.reduceLeft(this, callable);
    }

    public final String toString() {
        return Sequences.toString(this);
    }

    public final String toString(final String separator) {
        return Sequences.toString(this, separator);
    }

    public final String toString(final String start, final String separator, final String end) {
        return Sequences.toString(this, start, separator, end);
    }

    public final String toString(final String start, final String separator, final String end, final int limit) {
        return Sequences.toString(this, start, separator, end, limit);
    }

    public final Set<T> union(final Iterable<T> other) {
        return Sequences.union(Sequences.sequence(this, other));
    }

    public final boolean isEmpty() {
        return Sequences.isEmpty(this);
    }

    public final List<T> toList() {
        return Sequences.toList(this);
    }

    public final T[] toArray(final Class<T> aClass) {
        return toArray((T[]) Array.newInstance(aClass, 0));
    }

    public final T[] toArray(T[] array) {
        return toList().toArray(array);
    }

    public final Sequence<T> remove(final T t) {
        return Sequences.remove(this, t);
    }

    public final Number size() {
        return Sequences.size(this);
    }

    public final Sequence<T> take(final int count) {
        return Sequences.take(this, count);
    }

    public final Sequence<T> takeWhile(final Predicate<? super T> predicate) {
        return Sequences.takeWhile(this, predicate);
    }

    public final Sequence<T> drop(final int count) {
        return Sequences.drop(this, count);
    }

    public final Sequence<T> dropWhile(final Predicate<? super T> predicate) {
        return Sequences.dropWhile(this, predicate);
    }

    public final boolean forAll(final Predicate<? super T> predicate) {
        return Sequences.forAll(this, predicate);
    }

    public final boolean contains(final T t) {
        return Sequences.contains(this, t);
    }

    public final boolean exists(final Predicate<? super T> predicate) {
        return Sequences.exists(this, predicate);
    }

    public final Option<T> find(final Predicate<? super T> predicate) {
        return Sequences.find(this, predicate);
    }

    public final <S> Option<S> tryPick(final Callable1<T, Option<S>> callable) {
        return Sequences.tryPick(this, callable);
    }

    public final <S> S pick(final Callable1<T, Option<S>> callable) {
        return Sequences.pick(this, callable);
    }

    public final Sequence<T> add(final T t) {
        return Sequences.add(this, t);
    }

    public final Sequence<T> join(final Iterable<T> iterable) {
        return Sequences.join(this, iterable);
    }

    public final Sequence<T> cons(final T t) {
        return Sequences.cons(t, this);
    }

    public final MemorisedSequence<T> memorise() {
        return Sequences.memorise(this);
    }

    public final <T2> Sequence<Pair<T, T2>> zip(final Iterable<T2> iterable) {
        return Sequences.zip(this, iterable);
    }

    public final Sequence<Pair<Number, T>> zipWithIndex() {
        return Sequences.zipWithIndex(this);
    }

    public final Sequence<T> sortBy(final Callable1<T, ? extends Comparable> callable) {
        return Sequences.sortBy(this, callable);
    }

    public final Sequence<T> sortBy(final Comparator<? super T> comparator) {
        return Sequences.sortBy(this, comparator);
    }

    public final <S> Sequence<S> safeCast(final Class<S> aClass) {
        return Sequences.safeCast(this, aClass);
    }

    public final Sequence<T> realise() {
        return Sequences.realise(this);
    }

    public final Sequence<T> reverse() {
        return Sequences.reverse(this);
    }
}
