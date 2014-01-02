package com.googlecode.totallylazy;

import com.googlecode.totallylazy.collections.PersistentList;
import com.googlecode.totallylazy.iterators.FilterIterator;
import com.googlecode.totallylazy.iterators.FlattenIterator;
import com.googlecode.totallylazy.iterators.InitIterator;
import com.googlecode.totallylazy.iterators.IterateIterator;
import com.googlecode.totallylazy.iterators.MapIterator;
import com.googlecode.totallylazy.iterators.PartitionIterator;
import com.googlecode.totallylazy.iterators.PeekingIterator;
import com.googlecode.totallylazy.iterators.RangerIterator;
import com.googlecode.totallylazy.iterators.RepeatIterator;
import com.googlecode.totallylazy.iterators.TakeWhileIterator;
import com.googlecode.totallylazy.iterators.UnfoldRightIterator;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Appendables.append;
import static com.googlecode.totallylazy.Callables.nullGuard;
import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Predicates.in;
import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.onlyOnce;
import static com.googlecode.totallylazy.Predicates.whileTrue;
import static com.googlecode.totallylazy.Sequences.one;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.numbers.Numbers.increment;

public class Iterators {
    public static boolean equalsTo(Iterator<?> a, Iterator<?> b) {
        while (a.hasNext() && b.hasNext()) {
            Object aValue = a.next();
            Object bValue = b.next();

            if (aValue == bValue) {
                continue;
            }

            if (aValue == null || !aValue.equals(bValue)) {
                return false;
            }
        }
        return !(a.hasNext() || b.hasNext());
    }

    public static <T> boolean equalsTo(Iterator<? extends T> a, Iterator<? extends T> b, Predicate<? super Pair<T, T>> predicate) {
        while (a.hasNext() && b.hasNext()) {
            if (!predicate.matches(Pair.pair(a.next(), b.next()))) return false;
        }
        return !(a.hasNext() || b.hasNext());
    }

    public static <T> void each(final Iterator<? extends T> iterator, final Callable1<? super T, ?> runnable) {
        forEach(iterator, runnable);
    }

    public static <T> void forEach(final Iterator<? extends T> iterator, final Callable1<? super T, ?> runnable) {
        while (iterator.hasNext()) {
            Callers.call(runnable, iterator.next());
        }
    }

    public static <T, S> Iterator<S> map(final Iterator<? extends T> iterator, final Callable1<? super T, ? extends S> callable) {
        return new MapIterator<T, S>(iterator, callable);
    }

    public static <T, S> Iterator<S> flatMap(final Iterator<? extends T> iterator, final Callable1<? super T, ? extends Iterable<? extends S>> callable) {
        return flattenIterable(map(iterator, callable));
    }

    public static <T> Iterator<T> filter(final Iterator<? extends T> iterator, final Predicate<? super T> predicate) {
        return new FilterIterator<T>(iterator, predicate);
    }

    public static <T> Iterator<T> iterate(final Callable1<? super T, ? extends T> callable, final T t) {
        return new IterateIterator<T>(nullGuard(callable), t);
    }

    public static <T> T head(final Iterator<? extends T> iterator) {
        if (iterator.hasNext()) {
            return iterator.next();
        }
        throw new NoSuchElementException();
    }

    public static <T> Option<T> headOption(final Iterator<? extends T> iterator) {
        return iterator.hasNext() ? some(iterator.next()) : Option.<T>none();
    }

    public static <T> T last(final Iterator<? extends T> iterator) {
        return head(reverse(iterator));
    }

    public static <T> Option<T> lastOption(final Iterator<? extends T> iterator) {
        return headOption(reverse(iterator));
    }

    public static <T> Iterator<T> tail(final Iterator<? extends T> iterator) {
        if (iterator.hasNext()) {
            iterator.next();
            return new PeekingIterator<T>(iterator);
        }
        throw new NoSuchElementException();
    }

    public static <T> Iterator<T> init(final Iterator<? extends T> iterator) {
        return new InitIterator<T>(iterator);
    }

    public static <T, S> S fold(final Iterator<? extends T> iterator, final S seed, final Callable2<? super S, ? super T, ? extends S> callable) {
        return foldLeft(iterator, seed, callable);
    }

    public static <T, S> S foldLeft(final Iterator<? extends T> iterator, final S seed, final Callable2<? super S, ? super T, ? extends S> callable) {
        S accumulator = seed;
        while (iterator.hasNext()) {
            accumulator = call(callable, accumulator, iterator.next());
        }
        return accumulator;
    }

    public static <T, S> S foldRight(final Iterator<? extends T> iterator, final S seed, final Callable2<? super T, ? super S, ? extends S> callable) {
        Iterator<T> reversed = reverse(iterator);
        S accumilator = seed;
        while (reversed.hasNext()) {
            accumilator = call(callable, reversed.next(), accumilator);
        }
        return accumilator;
    }

    public static <T> Iterator<T> reverse(Iterator<? extends T> iterator) {
        return PersistentList.constructors.reverse(iterator).iterator();
    }

    public static <T, S> S foldRight(final Iterator<? extends T> iterator, final S seed, final Callable1<? super Pair<T, S>, ? extends S> callable) {
        if (!iterator.hasNext()) return seed;
        return Callers.call(callable, Pair.pair(returns(head(iterator)), new Function<S>() {
            @Override
            public S call() throws Exception {
                return foldRight(iterator, seed, callable);
            }
        }));
    }

    public static <T, S> S reduce(final Iterator<? extends T> iterator, final Callable2<? super S, ? super T, ? extends S> callable) {
        return reduceLeft(iterator, callable);
    }

    public static <T, S> S reduceLeft(final Iterator<? extends T> iterator, final Callable2<? super S, ? super T, ? extends S> callable) {
        return foldLeft(iterator, seed(iterator, callable), callable);
    }

    private static <T, S> S seed(Iterator<? extends T> iterator, Callable2<? super S, ? super T, ? extends S> callable) {
        if (callable instanceof Identity) return Unchecked.<Identity<S>>cast(callable).identity();
        return Unchecked.<S>cast(iterator.next());
    }

    public static <T, S> S reduceRight(final Iterator<? extends T> iterator, final Callable2<? super T, ? super S, ? extends S> callable) {
        return foldRight(iterator, Unchecked.<S>cast(iterator.next()), callable);
    }

    public static <T, S> S reduceRight(final Iterator<? extends T> iterator, final Callable1<? super Pair<T, S>, ? extends S> callable) {
        return foldRight(iterator, Unchecked.<S>cast(iterator.next()), callable);
    }

    public static String toString(final Iterator<?> iterator, final String separator) {
        return toString(iterator, "", separator, "");
    }

    public static String toString(final Iterator<?> iterator, final String start, final String separator, final String end) {
        return appendTo(iterator, new StringBuilder(), start, separator, end).toString();
    }

    public static <A extends Appendable> A appendTo(Iterator<?> iterator, A appendable) {
        return appendTo(iterator, appendable, ",");
    }

    public static <A extends Appendable> A appendTo(Iterator<?> iterator, A appendable, String separator) {
        return appendTo(iterator, appendable, "", separator, "");
    }

    public static <A extends Appendable> A appendTo(Iterator<?> iterator, A appendable, String start, String separator, String end) {
        append(start, appendable);
        if (iterator.hasNext()) append(String.valueOf(iterator.next()), appendable);
        while (iterator.hasNext()) {
            append(separator, appendable); // Do not single line as causes weird BUG with StringBuilder
            append(String.valueOf(iterator.next()), appendable);
        }
        return append(end, appendable);
    }

    public static <T> List<T> toList(final Iterator<? extends T> iterator) {
        final List<T> result = new ArrayList<T>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }

    public static <T> Deque<T> toDeque(final Iterator<? extends T> iterator) {
        final Deque<T> result = new ArrayDeque<T>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }

    public static <T> Iterator<T> repeat(final Callable<? extends T> callable) {
        return new RepeatIterator<T>(callable);
    }

    public static <T> Iterator<T> repeat(final T item) {
        return new RepeatIterator<T>(returns(item));
    }

    public static Iterator<Number> range(final Number start) {
        return iterate(increment, start);
    }

    public static Iterator<Number> range(final Number start, final Number end) {
        return new RangerIterator(start, end);
    }

    public static Iterator<Number> range(final Number start, final Number end, final Number step) {
        return new RangerIterator(start, end, step);
    }

    public static <T> Iterator<T> remove(final Iterator<? extends T> iterator, final T t) {
        return filter(iterator, not(onlyOnce(is(t))));
    }

    public static <T> Iterator<T> removeAll(final Iterator<? extends T> iterator, final Iterable<? extends T> remove) {
        return filter(iterator, not(in(remove)));
    }

    public static <T> Iterator<T> take(final Iterator<? extends T> iterator, final int count) {
        return takeWhile(iterator, Predicates.countTo(count));
    }

    public static <T> Iterator<T> takeWhile(final Iterator<? extends T> iterator, final Predicate<? super T> predicate) {
        return new TakeWhileIterator<T>(iterator, predicate);
    }

    public static <T> Iterator<T> drop(final Iterator<? extends T> iterator, final int count) {
        return dropWhile(iterator, Predicates.countTo(count));
    }

    public static <T> Iterator<T> dropWhile(final Iterator<? extends T> iterator, final Predicate<? super T> predicate) {
        return filter(iterator, not(whileTrue(predicate)));
    }

    public static <T> boolean forAll(final Iterator<? extends T> iterator, final Predicate<? super T> predicate) {
        while (iterator.hasNext()) {
            if (!predicate.matches(iterator.next())) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean contains(final Iterator<? extends T> iterator, final T t) {
        return exists(iterator, is(t));
    }

    public static <T> boolean exists(final Iterator<? extends T> iterator, final Predicate<? super T> predicate) {
        while (iterator.hasNext()) {
            boolean result = predicate.matches(iterator.next());
            if (result) {
                return true;
            }
        }
        return false;
    }

    public static <T> Option<T> find(final Iterator<? extends T> iterator, final Predicate<? super T> predicate) {
        while (iterator.hasNext()) {
            T item = iterator.next();
            boolean result = predicate.matches(item);
            if (result) {
                return some(item);
            }
        }
        return none();
    }

    public static <T, S> Option<S> tryPick(final Iterator<? extends T> iterator, final Callable1<? super T, ? extends Option<? extends S>> callable) {
        while (iterator.hasNext()) {
            T item = iterator.next();
            Option<S> result = Unchecked.cast(call(callable, item));
            if (!result.isEmpty()) {
                return result;
            }
        }
        return none();
    }

    public static <T, S> S pick(final Iterator<? extends T> iterator, final Callable1<? super T, ? extends Option<? extends S>> callable) {
        return tryPick(iterator, callable).get();
    }

    public static <T> Iterator<T> add(final Iterator<? extends T> iterator, final T t) {
        return join(iterator, sequence(t).iterator());
    }

    public static <T> Iterator<T> join(final Iterator<? extends T> first, final Iterator<? extends T> second) {
        return join(sequence(first, second));
    }

    public static <T> Iterator<T> join(final Iterator<? extends T> first, final Iterator<? extends T> second, final Iterator<? extends T> third) {
        return join(sequence(first, second, third));
    }

    public static <T> Iterator<T> join(final Iterator<? extends T> first, final Iterator<? extends T> second, final Iterator<? extends T> third, final Iterator<? extends T> fourth) {
        return join(sequence(first, second, third, fourth));
    }

    public static <T> Iterator<T> join(final Iterator<? extends T> first, final Iterator<? extends T> second, final Iterator<? extends T> third, final Iterator<? extends T> fourth, final Iterator<? extends T> fifth) {
        return join(sequence(first, second, third, fourth, fifth));
    }

    public static <T> Iterator<T> join(final Iterator<? extends T>... iterators) {
        return join(sequence(iterators));
    }

    public static <T> Iterator<T> join(final Iterable<? extends Iterator<? extends T>> iterable) {
        return flatten(iterable.iterator());
    }

    public static <T> Iterator<T> cons(final T t, final Iterator<? extends T> iterator) {
        return join(one(t).iterator(), iterator);
    }

    public static <T, S> Iterator<S> safeCast(final Iterator<? extends T> iterator, final Class<? extends S> aClass) {
        return map(filter(iterator, instanceOf(aClass)), Callables.cast(aClass));
    }

    public static <T, S> Iterator<S> unsafeCast(final Iterator<? extends T> iterator) {
        return map(iterator, Callables.<T, S>cast());
    }

    public static <T> int size(final Iterator<? extends T> iterator) {
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        return count;
    }

    public static <T> Number number(final Iterator<? extends T> iterator) {
        Number count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count = increment(count);
        }
        return count;
    }

    public static <T> Pair<Sequence<T>, Sequence<T>> partition(final Iterator<? extends T> iterator, final Predicate<? super T> predicate) {
        final Queue<T> matchedQueue = new ArrayDeque<T>();
        final Queue<T> unmatchedUnmatched = new ArrayDeque<T>();
        return Pair.pair(Sequences.memorise(new PartitionIterator<T>(iterator, predicate, matchedQueue, unmatchedUnmatched)),
                Sequences.memorise(new PartitionIterator<T>(iterator, Predicates.<T>not(predicate), unmatchedUnmatched, matchedQueue)));
    }

    public static <T> Pair<Sequence<T>, Sequence<T>> splitAt(final Iterator<? extends T> iterator, final Number index) {
        return partition(iterator, Predicates.countTo(index));
    }

    public static <T> Pair<Sequence<T>, Sequence<T>> splitWhen(final Iterator<? extends T> iterator, final Predicate<? super T> predicate) {
        Pair<Sequence<T>, Sequence<T>> partition = breakOn(iterator, predicate);
        return Pair.pair(partition.first(), partition.second().isEmpty() ? Sequences.<T>empty() : partition.second().tail());
    }

    public static <T> Pair<Sequence<T>, Sequence<T>> splitOn(final Iterator<? extends T> iterator, final T instance) {
        return splitWhen(iterator, is(instance));
    }

    public static <T> Pair<Sequence<T>, Sequence<T>> span(final Iterator<? extends T> iterator, final Predicate<? super T> predicate) {
        return partition(iterator, whileTrue(predicate));
    }

    public static <T> Pair<Sequence<T>, Sequence<T>> breakOn(final Iterator<? extends T> iterator, final Predicate<? super T> predicate) {
        return partition(iterator, whileTrue(Predicates.<T>not(predicate)));
    }

    public static <T, Key> Sequence<Group<Key, T>> groupBy(final Iterator<? extends T> iterator, final Callable1<? super T, ? extends Key> callable) {
        return Maps.entries(Maps.multiMap(iterator, callable)).map(new Callable1<Map.Entry<Key, List<T>>, Group<Key, T>>() {
            @Override
            public Group<Key, T> call(Map.Entry<Key, List<T>> entry) throws Exception {
                return new Group<Key, T>(entry.getKey(), entry.getValue());
            }
        });
    }

    public static <T> LogicalPredicate<Iterator<T>> hasNext() {
        return new LogicalPredicate<Iterator<T>>() {
            public boolean matches(Iterator<T> iterator) {
                return iterator.hasNext();
            }
        };
    }

    public static <T> Function1<Iterator<T>, T> next() {
        return new Function1<Iterator<T>, T>() {
            public T call(Iterator<T> iterator) throws Exception {
                return iterator.next();
            }
        };
    }

    public static <T> Iterator<T> flatten(final Iterator<? extends Iterator<? extends T>> iterator) {
        return new FlattenIterator<T>(iterator);
    }

    public static <T> Iterator<T> flattenIterable(final Iterator<? extends Iterable<? extends T>> iterator) {
        Iterator<Iterable<T>> noWildCards = unsafeCast(iterator);
        return flatten(map(noWildCards, Callables.<T>asIterator()));
    }

    public static <T> Iterator<T> interruptable(final Iterator<? extends T> iterator) {
        return map(iterator, Callables.<T>returnArgument().interruptable());
    }

    public static <A, B> Iterator<A> unfoldRight(Callable1<? super B, ? extends Option<? extends Pair<? extends A, ? extends B>>> callable, B seed) {
        return new UnfoldRightIterator<A, B>(callable, seed);
    }

    public static <T, C extends Segment<T>> C joinTo(Iterator<? extends T> iterator, C rest) {
        while (iterator.hasNext()) {
            rest = cast(rest.cons(iterator.next()));
        }
        return rest;
    }

    public static <T> T index(Iterator<? extends T> iterator, int index) {
        for (int i = 0; iterator.hasNext(); i++) {
            T next = iterator.next();
            if (i == index) return next;
        }
        throw new IndexOutOfBoundsException();
    }

    public static <T> int indexOf(Iterator<? extends T> iterator, T instance) {
        for (int i = 0; iterator.hasNext(); i++) {
            T next = iterator.next();
            if (instance.equals(next)) return i;
        }
        return -1;
    }

    public static class functions {
        public static <T> Function2<Iterable<? extends T>, Iterable<? extends T>, Iterable<T>> join() {
            return new Function2<Iterable<? extends T>, Iterable<? extends T>, Iterable<T>>() {
                @Override
                public Iterable<T> call(final Iterable<? extends T> a, final Iterable<? extends T> b) throws Exception {
                    return new Sequence<T>() {
                        @Override
                        public Iterator<T> iterator() {
                            return Iterators.join(a.iterator(), b.iterator());
                        }
                    };
                }
            };
        }
    }
}