package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.FilterIterator;
import com.googlecode.totallylazy.iterators.FlatMapIterator;
import com.googlecode.totallylazy.iterators.IterateIterator;
import com.googlecode.totallylazy.iterators.MapIterator;
import com.googlecode.totallylazy.iterators.PartitionIterator;
import com.googlecode.totallylazy.iterators.PeekingIterator;
import com.googlecode.totallylazy.iterators.RangerIterator;
import com.googlecode.totallylazy.iterators.RepeatIterator;
import com.googlecode.totallylazy.iterators.TakeWhileIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callables.cast;
import static com.googlecode.totallylazy.Callables.nullGuard;
import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.onlyOnce;
import static com.googlecode.totallylazy.Predicates.whileTrue;
import static com.googlecode.totallylazy.Sequences.memorise;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.numbers.Numbers.equalTo;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.numbers.Numbers.lessThan;

public class Iterators {
    public static boolean equalsTo(Iterator a, Iterator b){
        while(a.hasNext() && b.hasNext()){
            if(!a.next().equals(b.next())){
                return false;
            }
        }
        return !(a.hasNext() || b.hasNext());
    }

    public static <T> void forEach(final Iterator<T> iterator, final Callable1<? super T,Void> runnable) {
        while (iterator.hasNext()) {
            Callers.call(runnable, iterator.next());
        }
    }

    public static <T, S> Iterator<S> map(final Iterator<T> iterator, final Callable1<? super T, S> callable) {
        return new MapIterator<T, S>(iterator, callable);
    }

    public static <T, S> Iterator<S> flatMap(final Iterator<T> iterator, final Callable1<? super T, ? extends Iterable<S>> callable) {
        return new FlatMapIterator<T, S>(iterator, callable);
    }

    public static <T> Iterator<T> filter(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        return new FilterIterator<T>(iterator, predicate);
    }

    public static <T> Iterator<T> iterate(final Callable1<? super T, T> callable, final T t) {
        return new IterateIterator<T>(nullGuard(callable), t);
    }

    public static <T> T head(final Iterator<T> iterator) {
        if (iterator.hasNext()) {
            return iterator.next();
        }
        throw new NoSuchElementException();
    }

    public static <T> Option<T> headOption(final Iterator<T> iterator) {
        return iterator.hasNext() ? some(iterator.next()) : Option.<T>none();
    }

    public static <T> Iterator<T> tail(final Iterator<T> iterator) {
        if (iterator.hasNext()) {
            iterator.next();
            return new PeekingIterator<T>(iterator);
        }
        throw new NoSuchElementException();
    }

    public static <T, S> S fold(final Iterator<T> iterator, final S seed, final Callable2<? super S, ? super T, S> callable) {
        return foldLeft(iterator, seed, callable);
    }

    public static <T, S> S foldLeft(final Iterator<T> iterator, final S seed, final Callable2<? super S, ? super T, S> callable) {
        S accumulator = seed;
        while (iterator.hasNext()) {
            accumulator = call(callable, accumulator, iterator.next());
        }
        return accumulator;
    }

    public static <T,S> S reduce(final Iterator<T> iterator, final Callable2<? super S, ? super T, S> callable) {
        return reduceLeft(iterator, callable);
    }

    public static <T,S> S reduceLeft(final Iterator<T> iterator, final Callable2<? super S, ? super T, S> callable) {
        return foldLeft(iterator, (S) iterator.next(), callable);
    }

    public static String toString(final Iterator iterator) {
        return toString(iterator, ",");
    }

    public static String toString(final Iterator iterator, final String separator) {
        return toString(iterator, "", separator, "");
    }

    public static String toString(final Iterator iterator, final String start, final String separator, final String end) {
        return toString(iterator,start, separator, end, 100);
    }

    public static String toString(final Iterator iterator, final String start, final String separator, final String end, final Number limit) {
        Number count = 0;
        StringBuilder builder = new StringBuilder();
        builder.append(start);
        if (iterator.hasNext()) builder.append(iterator.next());
        count = increment(count);
        while (iterator.hasNext() && lessThan(count, limit)) {
            count = increment(count);
            builder.append(separator);
            builder.append(iterator.next());
        }
        if(equalTo(count, limit)) builder.append("...");
        builder.append(end);
        return builder.toString();
    }

    public static <T> List<T> toList(final Iterator<T> iterator) {
        final List<T> result = new ArrayList<T>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }

    public static <T> Iterator<T> repeat(final Callable<T> callable) {
        return new RepeatIterator<T>(callable);
    }

    public static <T> Iterator<T> repeat(final T item) {
        return new RepeatIterator<T>(returns(item));
    }

    public static Iterator<Number> range(final Number end) {
        return new RangerIterator(end);
    }

    public static Iterator<Number> range(final Number start, final Number end) {
        return new RangerIterator(start, end);
    }

    public static Iterator<Number> range(final Number start, final Number end, final Number step) {
        return new RangerIterator(start, end, step);
    }

    public static <T> Iterator<T> remove(final Iterator<T> iterator, final T t) {
        return filter(iterator, not(onlyOnce(is(t))));
    }

    public static <T> Iterator<T> take(final Iterator<T> iterator, final int count) {
        return takeWhile(iterator, Predicates.countTo(count));
    }

    public static <T> Iterator<T> takeWhile(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        return new TakeWhileIterator<T>(iterator, predicate);
    }

    public static <T> Iterator<T> drop(final Iterator<T> iterator, final int count) {
        return dropWhile(iterator, Predicates.countTo(count));
    }

    public static <T> Iterator<T> dropWhile(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        return filter(iterator, not(whileTrue(predicate)));
    }

    public static <T> boolean forAll(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        while(iterator.hasNext()){
            boolean result = predicate.matches(iterator.next());
            if(!result){
                return false;
            }
        }
        return true;
    }

    public static <T> boolean contains(final Iterator<T> iterator, final T t) {
        return exists(iterator, is(t));
    }

    public static <T> boolean exists(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        while(iterator.hasNext()){
            boolean result = predicate.matches(iterator.next());
            if(result){
                return true;
            }
        }
        return false;
    }

    public static <T> Option<T> find(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        while(iterator.hasNext()){
            T item = iterator.next();
            boolean result = predicate.matches(item);
            if(result){
                return some(item);
            }
        }
        return none();
    }

    public static <T,S> Option<S> tryPick(final Iterator<T> iterator, final Callable1<T, Option<S>> callable) {
        while(iterator.hasNext()){
            T item = iterator.next();
            Option<S> result = call(callable, item);
            if(!result.isEmpty()){
                return result;
            }
        }
        return none();
    }

    public static <T,S> S pick(final Iterator<T> iterator, final Callable1<T, Option<S>> callable) {
        return tryPick(iterator, callable).get();
    }

    public static <T> Iterator<T> add(final Iterator<T> iterator, final T t) {
        return join(iterator, sequence(t).iterator());
    }

    public static <T> Iterator<T> join(final Iterator<T>... iterators) {
        return join(sequence(iterators));
    }

    public static <T> Iterator<T> join(final Iterable<Iterator<T>> iterable) {
        return new FlatMapIterator<Iterator<T>,T>(iterable.iterator(), Callables.<T>asIterable());
    }

    public static <T> Iterator<T> cons(final T t, final Iterator<T> iterator) {
        return join(sequence(t).iterator(), iterator);
    }

    public static <T, S> Iterator<S> safeCast(final Iterator<T> iterator, final Class<S> aClass) {
        return map(filter(iterator, instanceOf(aClass)), cast(aClass));
    }

    public static <T> Number size(final Iterator<T> iterator) {
        Number count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count = increment(count);
        }
        return count;
    }

    public static <T> Pair<Sequence<T>, Sequence<T>> partition(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        final Queue<T> matchedQueue = new LinkedList<T>();
        final Queue<T> unmatchedUnmatched = new LinkedList<T>();
        return Pair.pair(memorise(new PartitionIterator<T>(iterator, predicate, matchedQueue, unmatchedUnmatched)),
                memorise(new PartitionIterator<T>(iterator, Predicates.<T>not(predicate), unmatchedUnmatched, matchedQueue)));
    }

    public static <T> Pair<Sequence<T>, Sequence<T>> splitAt(final Iterator<T> iterator, final Number index) {
        return partition(iterator, Predicates.countTo(index));
    }

    public static <T> Pair<Sequence<T>, Sequence<T>> splitWhen(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        Pair<Sequence<T>, Sequence<T>> partition = breakOn(iterator, predicate);
        return Pair.pair(partition.first(), partition.second().isEmpty() ? Sequences.<T>empty() : partition.second().tail());
    }

    public static <T> Pair<Sequence<T>, Sequence<T>> splitOn(final Iterator<T> iterator, final T instance) {
        return splitWhen(iterator, is(instance));
    }

    public static <T> Pair<Sequence<T>, Sequence<T>> span(Iterator<T> iterator, Predicate<? super T> predicate) {
        return partition(iterator, whileTrue(predicate));
    }

    public static <T> Pair<Sequence<T>, Sequence<T>> breakOn(Iterator<T> iterator, Predicate<? super T> predicate) {
        return partition(iterator, whileTrue(Predicates.<T>not(predicate)));
    }

    public static <T, Key> Sequence<Group<Key, T>> groupBy(final Iterator<T> iterator, final Callable1<? super T, Key> callable) {
        return sequence(Maps.toMap(iterator, callable).entrySet()).map(new Callable1<Map.Entry<Key, List<T>>, Group<Key, T>>() {
            public Group<Key, T> call(Map.Entry<Key, List<T>> entry) throws Exception {
                return new Group<Key, T>(entry.getKey(), entry.getValue());
            }
        });
    }
}
