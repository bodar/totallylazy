package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Predicate;

import java.util.NoSuchElementException;
import java.util.RandomAccess;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.collections.TreeVector.treeVector;
import static com.googlecode.totallylazy.numbers.Numbers.intValue;

public interface ImmutableVector<T> extends ImmutableList<T>, Indexed<T>, RandomAccess {
    @Override
    <S> ImmutableVector<S> map(Callable1<? super T, ? extends S> callable);

    @Override
    ImmutableVector<T> tail() throws NoSuchElementException;

    @Override
    ImmutableVector<T> cons(T head);

    @Override
    ImmutableVector<T> add(T value);

    @Override
    ImmutableVector<T> remove(T value);

    @Override
    ImmutableVector<T> removeAll(Iterable<T> values);

    @Override
    ImmutableVector<T> filter(Predicate<? super T> predicate);

    class constructors {
        public static <T> ImmutableVector<T> vector() {
            return treeVector(ImmutableSortedMap.constructors.<Integer, T>emptySortedMap());
        }

        public static <T> ImmutableVector<T> vector(T first) {
            return vector(sequence(first));
        }

        public static <T> ImmutableVector<T> vector(T first, T second) {
            return vector(sequence(first, second));
        }

        public static <T> ImmutableVector<T> vector(T first, T second, T third) {
            return vector(sequence(first, second, third));
        }

        public static <T> ImmutableVector<T> vector(T first, T second, T third, T fourth) {
            return vector(sequence(first, second, third, fourth));
        }

        public static <T> ImmutableVector<T> vector(T first, T second, T third, T fourth, T fifth) {
            return vector(sequence(first, second, third, fourth, fifth));
        }

        public static <T> ImmutableVector<T> vector(T... values) {
            return vector(sequence(values));
        }

        public static <T> ImmutableVector<T> vector(Iterable<? extends T> iterable) {
            return treeVector(ImmutableSortedMap.constructors.<Integer, T>sortedMap(sequence(iterable).
                    zipWithIndex().map(Callables.<Number, T, Integer>first(intValue))));
        }
    }
}