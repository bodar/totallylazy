package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.LazyCallable1;
import com.googlecode.totallylazy.iterators.SegmentIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callables.compose;
import static com.googlecode.totallylazy.Functions.returns;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.callables.LazyCallable1.lazy;

public class Computation<T> extends Sequence<T> implements Segment<T>, Memory {
    private final Lazy<Option<T>> head;
    private final LazyCallable1<T, Computation<T>> tail;

    private Computation(Callable<Option<T>> head, Callable1<T, Computation<T>> tail) {
        this.head = Lazy.lazy(head);
        this.tail = lazy(tail);
    }

    public static <T> Computation<T> computation1(Callable<Option<T>> callable, Callable1<T, Computation<T>> next) {
        return new Computation<T>(callable, next);
    }

    public static <T> Computation<T> computation1(T value, Callable1<T, Computation<T>> next) {
        return computation1(returns(some(value)), next);
    }

    public static <T> Computation<T> computation(Callable<Option<T>> callable, final Callable1<? super T, ? extends T> next) {
        return computation1(callable, generate(nextOption(next)));
    }

    private static <T> Callable1<T, Option<T>> nextOption(final Callable1<? super T, ? extends T> next) {
        return new Callable1<T, Option<T>>() {
            @Override
            public Option<T> call(T t) throws Exception {
                return Option.option(next.call(t));
            }
        };
    }

    public static <T> Computation<T> computation(T value, final Callable1<? super T, ? extends T> next) {
        return computation1(value, generate(nextOption(next)));
    }

    public static <T> Computation<T> cons(T value, Computation<T> next) {
        return computation1(returns(some(value)), Functions.<T, Computation<T>>constant(next));
    }

    public static <T> Computation<T> iterate(final Callable1<? super T, ? extends T> callable, final T t) {
        return computation(t, callable);
    }

    public static <T> Computation<T> memoize(final Iterable<? extends T> iterable) {
        return memorise(iterable);
    }

    public static <T> Computation<T> memorise(final Iterable<? extends T> iterable) {
        final Lazy<? extends Iterator<? extends T>> iterator = Lazy.lazy(new Callable<Iterator<? extends T>>() {
            @Override
            public Iterator<? extends T> call() throws Exception {
                return iterable.iterator();
            }
        });
        return Computation.<T>computation1(
                new Callable<Option<T>>() {
                    @Override
                    public Option<T> call() throws Exception {
                        return Iterators.headOption(iterator.value());
                    }
                },
                new Callable1<T, Computation<T>>() {
                    @Override
                    public Computation<T> call(T t) throws Exception {
                        return memorise(iterator.value());
                    }
                });
    }

    public static <T> Computation<T> memoize(final Iterator<? extends T> values) {
        return memorise(values);
    }

    public static <T> Computation<T> memorise(final Iterator<? extends T> iterator) {
        return Computation.<T>computation1(
                new Callable<Option<T>>() {
                    @Override
                    public Option<T> call() throws Exception {
                        return Iterators.headOption(iterator);
                    }
                },
                new Callable1<T, Computation<T>>() {
                    @Override
                    public Computation<T> call(T t) throws Exception {
                        return memorise(iterator);
                    }
                });
    }

    public static <T> Callable1<T, Computation<T>> generate(final Callable1<? super T, ? extends Option<T>> callable) {
        return new Callable1<T, Computation<T>>() {
            @Override
            public Computation<T> call(T value) throws Exception {
                return computation1(Callables.deferApply(callable, value), generate(callable));
            }
        };
    }

    @Override
    public Computation<T> cons(T t) {
        return cons(t, this);
    }

    @Override
    public <C extends Segment<T>> C joinTo(C rest) {
        return cast(tail().joinTo(rest).cons(head()));
    }

    @Override
    public Sequence<T> empty() {
        return Sequences.empty();
    }

    @Override
    public boolean isEmpty() {
        return head.apply().isEmpty();
    }

    @Override
    public T head() {
        return head.value().get();
    }

    @Override
    public Computation<T> tail() throws NoSuchElementException {
        return tail.apply(head());
    }

    @Override
    public Iterator<T> iterator() {
        return SegmentIterator.iterator(this);
    }

    @Override
    public void forget() {
        close();
    }

    @Override
    public void close() {
        head.close();
        tail.close();
    }

}
