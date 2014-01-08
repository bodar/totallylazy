package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.LazyCallable;
import com.googlecode.totallylazy.callables.LazyCallable1;
import com.googlecode.totallylazy.iterators.SegmentIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Functions.returns;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.callables.LazyCallable1.lazy;

public class Computation<T> extends Sequence<T> implements Segment<T>, Memory {
    private final LazyCallable<T> head;
    private final LazyCallable1<T, Computation<T>> tail;

    private Computation(Callable<T> head, Callable1<T, Computation<T>> tail) {
        this.head = LazyCallable.lazy(head);
        this.tail = lazy(tail);
    }

    public static <T> Computation<T> computation2(T value, Callable1<T, Computation<T>> next) {
        return computation1(returns(value), next);
    }

    public static <T> Computation<T> computation1(Callable<T> callable, Callable1<T, Computation<T>> next) {
        return new Computation<T>(callable, next);
    }

    public static <T> Computation<T> computation(Callable<T> callable, Callable1<? super T, ? extends T> next) {
        return computation1(callable, generate(next));
    }

    public static <T> Computation<T> computation(T value, Callable1<? super T, ? extends T> callable) {
        return computation2(value, generate(callable));
    }

    public static <T> Computation<T> computation(T value, Computation<T> next) {
        return computation1(returns(value), Functions.<T, Computation<T>>constant(next));
    }

    public static <T> Computation<T> iterate(final Callable1<? super T, ? extends T> callable, final T t) {
        return computation(t, callable);
    }

    public static <T> Computation<T> memoize(final Iterable<? extends T> iterable) {
        return memorise(iterable);
    }

    public static <T> Computation<T> memorise(final Iterable<? extends T> iterable) {
        final Callable<Iterator<T>> iterator = lazyIterator(iterable);
        return computation1(lazyHead(iterator), generate(lazyTail(iterator)));
    }

    public static <T> Computation<T> memoize(final Iterator<? extends T> values) {
        return memorise(values);
    }

    public static <T> Computation<T> memorise(final Iterator<? extends T> values) {
        final Function<Iterator<T>> iterator = returns(Unchecked.<Iterator<T>>cast(values));
        return computation1(lazyHead(iterator), generate(lazyTail(iterator)));
    }

    private static <T> UnaryFunction<T> lazyTail(final Callable<? extends Iterator<? extends T>> iterator) {
        return new UnaryFunction<T>() {
            @Override
            public T call(T t) throws Exception {
                return iterator.call().next();
            }
        };
    }

    private static <T> Function<T> lazyHead(final Callable<? extends Iterator<? extends T>> iterator) {
        return new Function<T>() {
            @Override
            public T call() throws Exception {
                return iterator.call().next();
            }
        };
    }

    private static <T> Function<Iterator<T>> lazyIterator(final Iterable<? extends T> iterable) {
        return new Function<Iterator<T>>() {
            @Override
            public Iterator<T> call() throws Exception {
                return Unchecked.cast(iterable.iterator());
            }
        }.lazy();
    }

    public static <T> Callable1<T, Computation<T>> generate(final Callable1<? super T, ? extends T> callable) {
        return new Callable1<T, Computation<T>>() {
            @Override
            public Computation<T> call(T value) throws Exception {
                return computation1(Callables.deferApply(callable, value), this);
            }
        };
    }

    @Override
    public Computation<T> cons(T t) {
        return computation(t, this);
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
        try {
            head.call();
            return false;
        } catch (NoSuchElementException e) {
            return true;
        } catch (Exception e) {
            throw LazyException.lazyException(e);
        }
    }

    @Override
    public T head() {
        return head.value();
    }

    @Override
    public Computation<T> tail() throws NoSuchElementException {
        return tail.apply(head.value());
    }

    @Override
    public Iterator<T> iterator() {
        return SegmentIterator.iterator(this);
    }

    @Override
    public void forget() {
        head.forget();
        tail.forget();
    }
}
