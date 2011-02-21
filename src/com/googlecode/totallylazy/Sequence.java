package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.List;
import java.util.Set;


public abstract class Sequence<T> implements Iterable<T>, First<T>, Second<T> {
    public void forEach(final Runnable1<T> runnable) {
        Sequences.forEach(this, runnable);
    }

    public <S> Sequence<S> map(final Callable1<? super T, S> callable) {
        return Sequences.map(this, callable);
    }

    public <T> Partition<T> partition(final Predicate<? super T> predicate) {
        return Sequences.partition((Iterable<T>) this, predicate);
    }

    public Sequence<T> filter(final Predicate<? super T> predicate) {
        return Sequences.filter(this, predicate);
    }

    public <S> Sequence<S> flatMap(final Callable1<? super T, Iterable<? extends S>> callable) {
        return Sequences.flatMap(this, callable);
    }

    public T first() {
        return Sequences.first(this);
    }

    public T last() {
        return Sequences.last(this);
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

    public <S> S fold(final S seed, final Callable2<? super S, ? super T, S> callable) {
        return Sequences.fold(this, seed, callable);
    }

    public <S> S foldLeft(final S seed, final Callable2<? super S, ? super T, S> callable) {
        return Sequences.foldLeft(this, seed, callable);
    }

    public T reduce(final Callable2<? super T, ? super T, T> callable) {
        return Sequences.reduce(this, callable);
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

    public String toString(final String start, final String separator, final String end, final Number limit) {
        return Sequences.toString(this, start, separator, end, limit);
    }

    public Set<T> union(final Iterable<? extends T> other) {
        return Sets.union(this.toSet(), Sets.set(other));
    }

    public Set<T> intersection(final Iterable<? extends T> other) {
        return Sets.intersection(this.toSet(), Sets.set(other));
    }

    public <S extends Set<T>> S toSet(S set) {
        return Sets.set(set, this);
    }
    public Set<T> toSet() {
        return Sets.set(this);
    }

    public boolean isEmpty() {
        return Sequences.isEmpty(this);
    }

    public List<T> toList() {
        return Sequences.toList(this);
    }

    public T[] toArray(final Class<T> aClass) {
        return toArray((T[]) Array.newInstance(aClass, 0));
    }

    public T[] toArray(T[] array) {
        return toList().toArray(array);
    }

    public Sequence<T> remove(final T t) {
        return Sequences.remove(this, t);
    }

    public Number size() {
        return Sequences.size(this);
    }

    public Sequence<T> take(final int count) {
        return Sequences.take(this, count);
    }

    public Sequence<T> takeWhile(final Predicate<? super T> predicate) {
        return Sequences.takeWhile(this, predicate);
    }

    public Sequence<T> drop(final int count) {
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

    public <S> Option<S> tryPick(final Callable1<T, Option<S>> callable) {
        return Sequences.tryPick(this, callable);
    }

    public <S> S pick(final Callable1<T, Option<S>> callable) {
        return Sequences.pick(this, callable);
    }

    public Sequence<T> add(final T t) {
        return Sequences.add(this, t);
    }

    public Sequence<T> join(final Iterable<? extends T> iterable) {
        return Sequences.join(this, iterable);
    }

    public Sequence<T> cons(final T t) {
        return Sequences.cons(t, this);
    }

    public MemorisedSequence<T> memorise() {
        return Sequences.memorise(this);
    }

    public ForwardOnlySequence<T> forwardOnly(){
        return Sequences.forwardOnly(this);
    }

    public <T2> Sequence<Pair<T, T2>> zip(final Iterable<T2> iterable) {
        return Sequences.zip(this, iterable);
    }

    public Sequence<Pair<Number, T>> zipWithIndex() {
        return Sequences.zipWithIndex(this);
    }

    public Sequence<T> sortBy(final Callable1<? super T, ? extends Comparable> callable) {
        return Sequences.sortBy(this, callable);
    }

    public Sequence<T> sortBy(final Comparator<? super T> comparator) {
        return Sequences.sortBy(this, comparator);
    }

    public <S> Sequence<S> safeCast(final Class<S> aClass) {
        return Sequences.safeCast(this, aClass);
    }

    public Sequence<T> realise() {
        return Sequences.realise(this);
    }

    public Sequence<T> reverse() {
        return Sequences.reverse(this);
    }

    public Sequence<T> cycle() {
        return Sequences.cycle(this);
    }
}