package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Foldable;
import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Sequences.sequence;

public interface PersistentList<T> extends Iterable<T>, Segment<T>, Functor<T>, PersistentCollection<T>, Indexed<T>, Foldable<T> {
    PersistentList<T> add(T value);

    PersistentList<T> remove(T value);

    PersistentList<T> removeAll(Iterable<T> values);

    List<T> toList();

    Sequence<T> toSequence();

    @Override
    PersistentList<T> cons(T head);

    @Override
    PersistentList<T> tail() throws NoSuchElementException;

    @Override
    <S> PersistentList<S> map(Callable1<? super T, ? extends S> callable);

    Option<T> find(Predicate<? super T> predicate);

    PersistentList<T> filter(Predicate<? super T> predicate);

    class constructors {
        public static <T> PersistentList<T> empty() {
            return LinkedList.empty();
        }

        public static <T> PersistentList<T> empty(Class<T> aClass) {
            return empty();
        }

        public static <T> PersistentList<T> cons(T head, PersistentList<T> tail) {
            return LinkedList.cons(head, tail);
        }

        public static <T> PersistentList<T> list(T one) {
            return cons(one, constructors.<T>empty());
        }

        public static <T> PersistentList<T> list(T one, T two) {
            return cons(one, cons(two, constructors.<T>empty()));
        }

        public static <T> PersistentList<T> list(T one, T two, T three) {
            return cons(one, cons(two, cons(three, constructors.<T>empty())));
        }

        public static <T> PersistentList<T> list(T one, T two, T three, T four) {
            return cons(one, cons(two, cons(three, cons(four, constructors.<T>empty()))));
        }

        public static <T> PersistentList<T> list(T one, T two, T three, T four, T five) {
            return cons(one, cons(two, cons(three, cons(four, cons(five, constructors.<T>empty())))));
        }

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
    }

    class functions {
        public static <T> Function2<PersistentList<T>, T, PersistentList<T>> cons() {
            return Segment.functions.cons();
        }

        public static <T> Function<PersistentList<T>> emptyPersistentList(Class<T> type) {
            return emptyPersistentList();
        }

        public static <T> Function<PersistentList<T>> emptyPersistentList() {
            return returns(PersistentList.constructors.<T>empty());
        }

        public static <T> Function1<PersistentList<T>, PersistentList<T>> cons(T t) {
            return Segment.functions.cons(t);
        }

        public static <T> Function1<PersistentList<T>, PersistentList<T>> tail() {
            return new Function1<PersistentList<T>, PersistentList<T>>() {
                @Override
                public PersistentList<T> call(PersistentList<T> list) throws Exception {
                    return list.tail();
                }
            };
        }

        public static <T> Function1<PersistentList<T>, PersistentList<T>> tail(Class<T> aClass) {
            return tail();
        }

        public static <T> Function1<PersistentList<T>, Option<T>> headOption(Class<T> aClass) {
            return headOption();
        }
        public static <T> Function1<PersistentList<T>, Option<T>> headOption() {
            return new Function1<PersistentList<T>, Option<T>>() {
                @Override
                public Option<T> call(PersistentList<T> list) throws Exception {
                    return list.headOption();
                }
            };
        }
    }
}
