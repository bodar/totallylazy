package com.googlecode.totallylazy;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.List;
import java.util.Set;


public abstract class Sequence<T> implements Iterable<T>, First<T>, Second<T> {
    public void forEach(Runnable1<T> runnable) {
        Sequences.forEach(this, runnable);
    }

    public <S> Sequence<S> map(final Callable1<? super T, S> callable) {
        return Sequences.map(this, callable);
    }

    public Sequence<T> filter(final Predicate<? super T> predicate) {
        return Sequences.filter(this, predicate);
    }

    public <S> Sequence<S> flatMap(final Callable1<? super T, Iterable<S>> callable) {
        return Sequences.flatMap(this, callable);
    }

    public T first() {
        return Sequences.first(this);
    }

    public T second() {
        return Sequences.second(this);
    }

    public T head() {
        return Sequences.head(this);
    }

    public Option<T> headOption() {
        return Sequences.headOption(this);
    }

    public Sequence<T> tail() {
        return Sequences.tail(this);
    }

    public <S> S foldLeft(final S seed, final Callable2<? super S, ? super T, S> callable) {
        return Sequences.foldLeft(this, seed, callable);
    }

    public T reduceLeft(final Callable2<? super T, ? super T, T> callable) {
        return Sequences.reduceLeft(this, callable);
    }

    public String toString() {
        return Sequences.toString(this);
    }

    public String toString(final String separator) {
        return Sequences.toString(this, separator);
    }

    public String toString(final String start, final String separator, final String end) {
        return Sequences.toString(this, start, separator, end);
    }

    public String toString(final String start, final String separator, final String end, int limit) {
        return Sequences.toString(this, start, separator, end, limit);
    }

    public Set<T> union(final Iterable<T> other) {
        return Sequences.union(Sequences.sequence(this, other));
    }

    public boolean isEmpty() {
        return Sequences.isEmpty(this);
    }

    public List<T> toList() {
        return Sequences.toList(this);
    }

    public T[] toArray(Class<T> aClass) {
        return toArray((T[]) Array.newInstance(aClass, 0));
    }

    public T[] toArray(T[] array) {
        return toList().toArray(array);
    }

    public Sequence<T> remove(T t) {
        return Sequences.remove(this, t);
    }

    public int size() {
        return Sequences.size(this);
    }

    public Sequence<T> take(int count) {
        return Sequences.take(this, count);
    }

    public Sequence<T> takeWhile(final Predicate<? super T> predicate) {
        return Sequences.takeWhile(this, predicate);
    }

    public Sequence<T> drop(int count) {
        return Sequences.drop(this, count);
    }

    public Sequence<T> dropWhile(final Predicate<? super T> predicate) {
        return Sequences.dropWhile(this, predicate);
    }

    public boolean forAll(final Predicate<? super T> predicate) {
        return Sequences.forAll(this, predicate);
    }

    public boolean contains(final T t) {
        return Sequences.contains(this, t);
    }

    public boolean exists(final Predicate<? super T> predicate) {
        return Sequences.exists(this, predicate);
    }

    public Option<T> find(final Predicate<? super T> predicate) {
        return Sequences.find(this, predicate);
    }

    public <S> Option<S> tryPick(Callable1<T, Option<S>> callable) {
        return Sequences.tryPick(this, callable);
    }

    public <S> S pick(Callable1<T, Option<S>> callable) {
        return Sequences.pick(this, callable);
    }

    public Sequence<T> add(T t) {
        return Sequences.add(this, t);
    }

    public Sequence<T> join(final Iterable<T> iterable) {
        return Sequences.join(this, iterable);
    }

    public Sequence<T> cons(final T t) {
        return Sequences.cons(t, this);
    }

    public MemorisedSequence<T> memorise() {
        return Sequences.memorise(this);
    }

    public <T2> Sequence<Pair<T, T2>> zip(final Iterable<T2> iterable) {
        return Sequences.zip(this, iterable);
    }

    public Sequence<Pair<Number, T>> zipWithIndex() {
        return Sequences.zipWithIndex(this);
    }

    public Sequence<T> sortBy(Callable1<T, ? extends Comparable> callable) {
        return Sequences.sortBy(this, callable);
    }

    public Sequence<T> sortBy(final Comparator<? super T> comparator) {
        return Sequences.sortBy(this, comparator);
    }

    public <S> Sequence<S> safeCast(Class<S> aClass) {
        return Sequences.safeCast(this, aClass);
    }

    public Sequence<T> realise() {
        return Sequences.realise(this);
    }
}
