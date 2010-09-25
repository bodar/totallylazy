package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callables.call;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Predicates.countTo;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.whileTrue;

public class Iterators {
    public static <T> void foreach(final Iterator<T> iterator, final Runnable1<T> runnable) {
        while (iterator.hasNext()) {
            runnable.run(iterator.next());
        }
    }

    public static <T, S> Iterator<S> map(final Iterator<T> iterator, final Callable1<? super T, S> callable) {
        return new MapIterator<T, S>(iterator, callable);
    }

    public static <T, S> Iterator<S> flatMap(final Iterator<T> iterator, final Callable1<? super T, Iterable<S>> callable) {
        return new FlatMapIterator<T, S>(iterator, callable);
    }

    public static <T> Iterator<T> filter(final Iterator<T> iterator, final Predicate<T> predicate) {
        return new FilterIterator<T>(iterator, predicate);
    }


    public static <T> Iterator<T> iterate(final Callable1<? super T, T> callable, final T t) {
        return new IterateIterator<T>(callable, t);
    }

    public static <T> T head(final Iterator<T> iterator) {
        if (iterator.hasNext()) {
            return iterator.next();
        }
        throw new NoSuchElementException();
    }

    public static <T> Option<T> headOption(Iterator<T> iterator) {
        return iterator.hasNext() ? some(iterator.next()) : Option.<T>none();
    }

    public static <T> Iterator<T> tail(final Iterator<T> iterator) {
        if (iterator.hasNext()) {
            iterator.next();
            return new DelegatingIterator<T>(iterator);
        }
        throw new NoSuchElementException();
    }

    public static <T, S> S foldLeft(final Iterator<T> iterator, final S seed, final Callable2<S, T, S> callable) {
        S accumilator = seed;
        while (iterator.hasNext()) {
            accumilator = call(callable, accumilator, iterator.next());
        }
        return accumilator;
    }

    public static <T> T reduceLeft(final Iterator<T> iterator, final Callable2<T, T, T> callable) {
        return foldLeft(iterator, iterator.next(), callable);
    }

    public static String toString(final Iterator iterator) {
        return toString(iterator, ",");
    }

    public static String toString(final Iterator iterator, final String separator) {
        return toString(iterator, "", separator, "");
    }

    public static String toString(final Iterator iterator, final String start, final String separator, final String end) {
        StringBuilder builder = new StringBuilder();
        builder.append(start);
        if (iterator.hasNext()) builder.append(iterator.next());
        while (iterator.hasNext()) {
            builder.append(separator);
            builder.append(iterator.next());
        }
        builder.append(end);
        return builder.toString();
    }

    public static <T> Set<T> union(final Iterable<Iterator<T>> iterators) {
        Set<T> result = new HashSet<T>();
        for (Iterator<T> iterator : iterators) {
            while (iterator.hasNext()) {
                result.add(iterator.next());
            }
        }
        return result;
    }

    public static <T> List<T> toList(final Iterator<T> iterator) {
        final List<T> result = new ArrayList<T>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }

    public static Iterator<Integer> range(int end) {
        return new RangerIterator(end);
    }

    public static Iterator<Integer> range(final int start, final int end) {
        return new RangerIterator(start, end);
    }

    public static Iterator<Integer> range(final int start, final int end, final int step) {
        return new RangerIterator(start, end, step);
    }

    public static <T> Iterator<T> remove(final Iterator<T> iterator, final T t) {
        return filter(iterator, not(t));
    }

    public static <T> Iterator<T> take(final Iterator<T> iterator, final int count) {
        return takeWhile(iterator, Predicates.<T>countTo(count));
    }

    public static <T> Iterator<T> takeWhile(final Iterator<T> iterator, final Predicate<T> predicate) {
        return new TakeWhileIterator<T>(iterator, predicate);
    }

    public static <T> Iterator<T> drop(final Iterator<T> iterator, final int count) {
        return dropWhile(iterator, Predicates.<T>countTo(count));
    }

    public static <T> Iterator<T> dropWhile(final Iterator<T> iterator, final Predicate<T> predicate) {
        return filter(iterator, not(whileTrue(predicate)));
    }

    public static <T> boolean forAll(Iterator<T> iterator, Predicate<T> predicate) {
        while(iterator.hasNext()){
            boolean result = predicate.matches(iterator.next());
            if(!result){
                return false;
            }
        }
        return true;
    }

    public static <T> boolean exists(Iterator<T> iterator, Predicate<T> predicate) {
        while(iterator.hasNext()){
            boolean result = predicate.matches(iterator.next());
            if(result){
                return true;
            }
        }
        return false;
    }

    public static <T> Option<T> find(Iterator<T> iterator, Predicate<T> predicate) {
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

}
