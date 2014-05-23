package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Filterable;
import com.googlecode.totallylazy.Foldable;
import com.googlecode.totallylazy.Returns;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Seq;
import com.googlecode.totallylazy.Sequence;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Sequences.sequence;

public interface PersistentList<T> extends List<T>, PersistentCollection<T>, Iterable<T>, Segment<T>, Functor<T>, Indexed<T>, Foldable<T>, Filterable<T> {
    Option<T> find(Predicate<? super T> predicate);

    @Override
    PersistentList<T> empty();

    @Override
    PersistentList<T> cons(T head);

    @Override
    PersistentList<T> tail() throws NoSuchElementException;

    PersistentList<T> append(T value);

    @Override
    PersistentList<T> delete(T value);

    PersistentList<T> deleteAll(Iterable<? extends T> values);

    PersistentList<T> reverse();

    @Override
    <S> PersistentList<S> map(Function<? super T, ? extends S> callable);

    @Override
    PersistentList<T> filter(final Predicate<? super T> predicate);

    List<T> toMutableList();

    Sequence<T> toSequence();

    Zipper<T> zipper();

    @Override
    T get(int index) throws IndexOutOfBoundsException;

    @Override
    PersistentList<T> subList(int fromIndex, int toIndex);

    @Override
    default boolean containsAll(Collection<?> c) { return PersistentCollection.super.containsAll(c);
    }

    @Override
    default <R> R[] toArray(R[] a) { return PersistentCollection.super.toArray(a) ;}

    @Override
    default Object[] toArray() { return PersistentCollection.super.toArray(); }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#cons(T)} */
    @Override @Deprecated
    default boolean add(T e) {
        throw new IllegalMutationException();
    }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#cons(T)} */
    @Override @Deprecated
    default boolean addAll(Collection<? extends T> c) {
        throw new IllegalMutationException();
    }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#delete(T)} */
    @Override @Deprecated
    default boolean remove(Object o) {
        throw new IllegalMutationException();
    }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#deleteAll(Iterable)} */
    @Override @Deprecated
    default boolean removeAll(Collection<?> c) {
        throw new IllegalMutationException();
    }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#filter(com.googlecode.totallylazy.Predicate)} */
    @Override @Deprecated
    default boolean retainAll(Collection<?> c) {
        throw new IllegalMutationException();
    }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#empty()} */
    @Override @Deprecated
    default void clear() {
        throw new IllegalMutationException();
    }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentList#append(T)} */
    @Override @Deprecated
    default void add(int index, T element) { throw new IllegalMutationException(); }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentList#append(T)} */
    @Override @Deprecated
    default boolean addAll(int index, Collection<? extends T> c) { throw new IllegalMutationException(); }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentList#append(T)} */
    @Override @Deprecated
    default T set(int index, T element) { throw new IllegalMutationException(); };

    /** @deprecated Mutation not supported. Replaced by {@link PersistentList#append(T)} */
    @Override @Deprecated
    default T remove(int index) { throw new IllegalMutationException(); }

    class constructors {
        public static <T> PersistentList<T> empty() {
            return LinkedList.emptyList();
        }

        public static <T> PersistentList<T> empty(Class<T> aClass) {
            return empty();
        }

        public static <T> PersistentList<T> cons(T head, PersistentList<T> tail) {
            return LinkedList.cons(head, tail);
        }

        public static <T> PersistentList<T> one(T one) {
            return cons(one, constructors.<T>empty());
        }

        @SafeVarargs
        public static <T> PersistentList<T> list(T... values) {
            return list(sequence(values));
        }

        public static <T> PersistentList<T> list(Iterable<? extends T> values) {
            return sequence(values).reverse().foldLeft(constructors.<T>empty(), functions.<T>cons());
        }

        public static <T> PersistentList<T> reverse(Iterable<? extends T> values) {
            return reverse(values.iterator());
        }

        public static <T> PersistentList<T> reverse(Iterator<? extends T> iterator) {
            PersistentList<T> reverse = empty();
            while (iterator.hasNext()) {
                reverse = cons(iterator.next(), reverse);
            }
            return reverse;
        }

        public static <T> PersistentList<T> reverse(PersistentList<T> list) {
            PersistentList<T> reverse = list.empty();
            for (T element : list) {
                reverse = reverse.cons(element);
            }
            return reverse;
        }
    }

    class functions {
        public static <T> Function2<PersistentList<T>, T, PersistentList<T>> cons() {
            return Segment.functions.cons();
        }

        public static <T> Returns<PersistentList<T>> emptyPersistentList(Class<T> type) {
            return emptyPersistentList();
        }

        public static <T> Returns<PersistentList<T>> emptyPersistentList() {
            return returns(PersistentList.constructors.<T>empty());
        }

        public static <T> Function<PersistentList<T>, PersistentList<T>> cons(T t) {
            return Segment.functions.cons(t);
        }

        public static <T> Function<PersistentList<T>, PersistentList<T>> tail() {
            return new Function<PersistentList<T>, PersistentList<T>>() {
                @Override
                public PersistentList<T> call(PersistentList<T> list) throws Exception {
                    return list.tail();
                }
            };
        }

        public static <T> Function<PersistentList<T>, PersistentList<T>> tail(Class<T> aClass) {
            return tail();
        }

        public static <T> Function<PersistentList<T>, Option<T>> headOption(Class<T> aClass) {
            return headOption();
        }
        public static <T> Function<PersistentList<T>, Option<T>> headOption() {
            return new Function<PersistentList<T>, Option<T>>() {
                @Override
                public Option<T> call(PersistentList<T> list) throws Exception {
                    return list.headOption();
                }
            };
        }
    }
}
