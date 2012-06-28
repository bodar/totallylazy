package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Container;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Sequences.sequence;

public interface ImmutableList<T> extends Iterable<T>, Segment<T>, Functor<T>, Container<T> {
    ImmutableList<T> add(T value);

    ImmutableList<T> remove(T value);

    ImmutableList<T> removeAll(Iterable<T> values);

    int size();

    List<T> toList();

    Sequence<T> toSequence();

    @Override
    ImmutableList<T> cons(T head);

    @Override
    ImmutableList<T> tail() throws NoSuchElementException;

    @Override
    <S> ImmutableList<S> map(Callable1<? super T, ? extends S> callable);

    Option<T> find(Predicate<? super T> predicate);

    ImmutableList<T> filter(Predicate<? super T> predicate);

    class constructors {
        public static <T> ImmutableList<T> empty() {
            return PersistentList.empty();
        }

        public static <T> ImmutableList<T> cons(T head, ImmutableList<T> tail) {
            return PersistentList.cons(head, tail);
        }

        public static <T> ImmutableList<T> list(T one) {
            return cons(one, constructors.<T>empty());
        }

        public static <T> ImmutableList<T> list(T one, T two) {
            return cons(one, cons(two, constructors.<T>empty()));
        }

        public static <T> ImmutableList<T> list(T one, T two, T three) {
            return cons(one, cons(two, cons(three, constructors.<T>empty())));
        }

        public static <T> ImmutableList<T> list(T one, T two, T three, T four) {
            return cons(one, cons(two, cons(three, cons(four, constructors.<T>empty()))));
        }

        public static <T> ImmutableList<T> list(T one, T two, T three, T four, T five) {
            return cons(one, cons(two, cons(three, cons(four, cons(five, constructors.<T>empty())))));
        }

        public static <T> ImmutableList<T> list(T... values) {
            return list(sequence(values));
        }

        public static <T> ImmutableList<T> list(Iterable<? extends T> values) {
            return sequence(values).reverse().foldLeft(constructors.<T>empty(), functions.<T, ImmutableList<T>>cons());
        }

        public static <T> ImmutableList<T> reverse(Iterable<? extends T> values) {
            return reverse(values.iterator());
        }

        public static <T> ImmutableList<T> reverse(Iterator<? extends T> iterator) {
            ImmutableList<T> reverse = empty();
            while (iterator.hasNext()) {
                reverse = cons(iterator.next(), reverse);
            }
            return reverse;
        }
    }
}