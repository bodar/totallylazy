package com.googlecode.totallylazy;

import com.googlecode.totallylazy.collections.ImmutableList;
import com.googlecode.totallylazy.collections.Indexed;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

import static com.googlecode.totallylazy.Callables.asHashCode;
import static com.googlecode.totallylazy.Callables.ascending;
import static com.googlecode.totallylazy.Callables.returnArgument;
import static com.googlecode.totallylazy.Sequences.sequence;


public abstract class Sequence<T> implements Iterable<T>, First<T>, Second<T>, Third<T>, Functor<T>, Segment<T>, ImmutableCollection<T>, Applicative<T>, Foldable<T>, Indexed<T> {
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Iterable) return Sequences.equalTo(this, (Iterable<?>) obj);
        return obj instanceof Segment && methods.equalTo(this, (Segment<?>) obj);
    }

    // Thread-safe Racy Single Check Idiom (Effective Java 2nd Edition p.284)
    private int hashCode;

    @Override
    public int hashCode() {
        if (hashCode == 0) {
            hashCode = fold(31, asHashCode());
        }
        return hashCode;
    }

    public void eachConcurrently(final Callable1<? super T, ?> runnable) {
        forEachConcurrently(runnable);
    }

    public void forEachConcurrently(final Callable1<? super T, ?> runnable) {
        Sequences.forEachConcurrently(this, runnable);
    }

    public void eachConcurrently(final Callable1<? super T, ?> runnable, Executor executor) {
        forEachConcurrently(runnable, executor);
    }

    public void forEachConcurrently(final Callable1<? super T, ?> runnable, Executor executor) {
        Sequences.forEachConcurrently(this, runnable, executor);
    }

    public void each(final Callable1<? super T, ?> runnable) {
        forEach(runnable);
    }

    public void forEach(final Callable1<? super T, ?> runnable) {
        Sequences.forEach(this, runnable);
    }

    public <S> Sequence<S> mapConcurrently(final Callable1<? super T, S> callable) {
        return Sequences.mapConcurrently(this, callable);
    }

    public <S> Sequence<S> mapConcurrently(final Callable1<? super T, S> callable, final Executor executor) {
        return Sequences.mapConcurrently(this, callable, executor);
    }

    @Override
    public <S> Sequence<S> map(final Callable1<? super T, ? extends S> callable) {
        return Sequences.map(this, callable);
    }

    public Pair<Sequence<T>, Sequence<T>> partition(final Predicate<? super T> predicate) {
        return Sequences.partition(this, predicate);
    }

    public Sequence<T> filter(final Predicate<? super T> predicate) {
        return Sequences.filter(this, predicate);
    }

    public <S> Sequence<S> flatMap(final Callable1<? super T, ? extends Iterable<? extends S>> callable) {
        return Sequences.flatMap(this, callable);
    }

    public <S> Sequence<S> flatMapConcurrently(final Callable1<? super T, ? extends Iterable<? extends S>> callable) {
        return Sequences.flatMapConcurrently(this, callable);
    }

    public <S> Sequence<S> flatMapConcurrently(final Callable1<? super T, ? extends Iterable<? extends S>> callable, final Executor executor) {
        return Sequences.flatMapConcurrently(this, callable, executor);
    }

    public <B> Sequence<B> applicate(final Sequence<? extends Callable1<? super T, ? extends B>> applicator) {
        return Sequences.applicate(applicator, this);
    }

    public T first() {
        return Sequences.first(this);
    }

    public T last() {
        return Sequences.last(this);
    }

    public Option<T> lastOption() {
        return Sequences.lastOption(this);
    }

    public T second() {
        return Sequences.second(this);
    }

    @Override
    public T third() {
        return Sequences.third(this);
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

    public Sequence<T> init() {
        return Sequences.init(this);
    }

    public <S> S fold(final S seed, final Callable2<? super S, ? super T, ? extends S> callable) {
        return Sequences.fold(this, seed, callable);
    }

    public <S> S foldLeft(final S seed, final Callable2<? super S, ? super T, ? extends S> callable) {
        return Sequences.foldLeft(this, seed, callable);
    }

    public <S> S foldRight(final S seed, final Callable2<? super T, ? super S, ? extends S> callable) {
        return Sequences.foldRight(this, seed, callable);
    }

    public <S> S foldRight(final S seed, final Callable1<? super Pair<T, S>, ? extends S> callable) {
        return Sequences.foldRight(this, seed, callable);
    }

    public <S> S reduce(final Callable2<? super S, ? super T, ? extends S> callable) {
        return Sequences.reduce(this, callable);
    }

    public <S> S reduceLeft(final Callable2<? super S, ? super T, ? extends S> callable) {
        return Sequences.reduceLeft(this, callable);
    }

    public <S> S reduceRight(final Callable2<? super T, ? super S, ? extends S> callable) {
        return Sequences.reduceRight(this, callable);
    }

    public <S> S reduceRight(final Callable1<? super Pair<T, S>, ? extends S> callable) {
        return Sequences.reduceRight(this, callable);
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

    public Set<T> union(final Iterable<? extends T> other) {
        return Sets.union(toSet(), Sets.set(other));
    }

    public Set<T> intersection(final Iterable<? extends T> other) {
        return Sets.intersection(sequence(toSet(), Sets.set(other)));
    }

    public <S extends Set<T>> S toSet(S set) {
        return Sets.set(set, this);
    }

    public Set<T> toSet() {
        return toSet(new LinkedHashSet<T>());
    }

    public Sequence<T> unique() {
        return unique(returnArgument());
    }

    public <S> Sequence<T> unique(Callable1<? super T, ? extends S> callable) {
        return Sequences.unique(this, callable);
    }

    public boolean isEmpty() {
        return Sequences.isEmpty(this);
    }

    public List<T> toList() {
        return Sequences.toList(this);
    }

    public List<T> toSortedList(Comparator<T> comparator) {
        return Sequences.toSortedList(this, comparator);
    }

    public Deque<T> toDeque() {
        return Sequences.toDeque(this);
    }

    public T[] toArray(final Class<?> aClass) {
        return toArray(Unchecked.<T[]>cast(Array.newInstance(aClass, 0)));
    }

    public T[] toArray(T[] array) {
        return toList().toArray(array);
    }

    public Sequence<T> remove(final T t) {
        return Sequences.remove(this, t);
    }

    @Override
    public int size() {
        return Sequences.size(this);
    }

    public Number number() {
        return Sequences.number(this);
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

    public <S> Option<S> tryPick(final Callable1<? super T, ? extends Option<? extends S>> callable) {
        return Sequences.tryPick(this, callable);
    }

    public <S> S pick(final Callable1<? super T, ? extends Option<? extends S>> callable) {
        return Sequences.pick(this, callable);
    }

    public Sequence<T> add(final T t) {
        return Sequences.add(this, t);
    }

    public Sequence<T> join(final Iterable<? extends T> iterable) {
        return Sequences.join(this, iterable);
    }

    @Override
    public <C extends Segment<T>> C joinTo(C rest) {
        return Sequences.joinTo(this, rest);
    }

    public Sequence<T> cons(final T t) {
        return Sequences.cons(t, this);
    }

    public Sequence<T> memorise() {
        return Sequences.memorise(this);
    }

    public ForwardOnlySequence<T> forwardOnly() {
        return Sequences.forwardOnly(this);
    }

    public <S> Sequence<Pair<T, S>> zip(final Iterable<? extends S> second) {
        return Sequences.zip(this, second);
    }

    public Sequence<Sequence<T>> transpose(final Iterable<? extends T>... iterables) {
        return transpose(sequence(iterables));
    }

    public Sequence<Sequence<T>> transpose(final Iterable<? extends Iterable<? extends T>> iterables) {
        return Sequences.transpose(Sequences.cons(this, sequence(iterables).<Iterable<T>>unsafeCast()));
    }

    public <S, Th> Sequence<Triple<T, S, Th>> zip(final Iterable<? extends S> second, final Iterable<? extends Th> third) {
        return Sequences.zip(this, second, third);
    }

    public <S, Th, Fo> Sequence<Quadruple<T, S, Th, Fo>> zip(final Iterable<? extends S> second, final Iterable<? extends Th> third, final Iterable<? extends Fo> fourth) {
        return Sequences.zip(this, second, third, fourth);
    }

    public <S, Th, Fo, Fi> Sequence<Quintuple<T, S, Th, Fo, Fi>> zip(final Iterable<? extends S> second, final Iterable<? extends Th> third, final Iterable<? extends Fo> fourth, final Iterable<? extends Fi> fifth) {
        return Sequences.zip(this, second, third, fourth, fifth);
    }

    public Sequence<Pair<Number, T>> zipWithIndex() {
        return Sequences.zipWithIndex(this);
    }

    public <R extends Comparable<? super R>> Sequence<T> sortBy(final Callable1<? super T, ? extends R> callable) {
        return sortBy(ascending(callable));
    }

    public Sequence<T> sortBy(final Comparator<? super T> comparator) {
        return Sequences.sortBy(this, comparator);
    }

    public <S> Sequence<S> safeCast(final Class<? extends S> aClass) {
        return Sequences.safeCast(this, aClass);
    }

    public <S> Sequence<S> unsafeCast() {
        return Sequences.unsafeCast(this);
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

    public <K> Map<K, List<T>> toMap(final Callable1<? super T, ? extends K> callable) {
        return Maps.multiMap(this, callable);
    }

    public <K> Sequence<Group<K, T>> groupBy(final Callable1<? super T, ? extends K> callable) {
        return Sequences.groupBy(this, callable);
    }

    public Sequence<Sequence<T>> recursive(final Callable1<Sequence<T>, Pair<Sequence<T>, Sequence<T>>> callable) {
        return Sequences.recursive(this, callable);
    }

    public Pair<Sequence<T>, Sequence<T>> splitAt(final Number index) {
        return Sequences.splitAt(this, index);
    }

    public Pair<Sequence<T>, Sequence<T>> splitWhen(final Predicate<? super T> predicate) {
        return Sequences.splitWhen(this, predicate);
    }

    public Pair<Sequence<T>, Sequence<T>> splitOn(final T instance) {
        return Sequences.splitOn(this, instance);
    }

    public Pair<Sequence<T>, Sequence<T>> span(final Predicate<? super T> predicate) {
        return Sequences.span(this, predicate);
    }

    public Pair<Sequence<T>, Sequence<T>> breakOn(final Predicate<? super T> predicate) {
        return Sequences.breakOn(this, predicate);
    }

    public Sequence<T> shuffle() {
        return Sequences.shuffle(this);
    }

    public Sequence<T> interruptable() {
        return Sequences.interruptable(this);
    }

    public ImmutableList<T> toImmutableList() {
        return ImmutableList.constructors.list(this);
    }

    public Sequence<Pair<T, T>> cartesianProduct() {
        return Sequences.cartesianProduct(this);
    }

    public <S> Sequence<Pair<T, S>> cartesianProduct(final Iterable<? extends S> other) {
        return Sequences.cartesianProduct(this, other);
    }

    public T get(int index) {
        return drop(index).head();
    }

    public Sequence<Sequence<T>> windowed(int size) {
        return Sequences.windowed(this, size);
    }

    public Sequence<T> intersperse(T separator) {
        return Sequences.intersperse(this, separator);
    }

    public Option<Sequence<T>> flatOption() {
        return Sequences.flatOption(this);
    }

    @Override
    public T index(int i) throws IndexOutOfBoundsException {
        return Sequences.index(this, i);
    }

    @Override
    public int indexOf(T t) {
        return Sequences.indexOf(this, t);
    }
}