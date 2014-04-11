package com.googlecode.totallylazy;

import com.googlecode.totallylazy.collections.Indexed;
import com.googlecode.totallylazy.collections.PersistentCollection;
import com.googlecode.totallylazy.collections.PersistentList;

import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

import static com.googlecode.totallylazy.Callables.ascending;
import static com.googlecode.totallylazy.Callables.returnArgument;
import static com.googlecode.totallylazy.Predicates.in;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Sequences.sequence;

public interface Seq<T> extends Iterable<T>, First<T>, Second<T>, Third<T>, Functor<T>, Segment<T>, PersistentCollection<T>, Applicative<T>, Monad<T>, Foldable<T>, Indexed<T>, Filterable<T> {
    default boolean equals(Iterable<? extends T> other, Predicate<? super Pair<T, T>> predicate) {
        return Sequences.equalTo(this, other, predicate);
    }

    default void eachConcurrently(final Function<? super T, ?> runnable) {
        forEachConcurrently(runnable);
    }

    default void forEachConcurrently(final Function<? super T, ?> runnable) {
        Sequences.forEachConcurrently(this, runnable);
    }

    default void eachConcurrently(final Function<? super T, ?> runnable, Executor executor) {
        forEachConcurrently(runnable, executor);
    }

    default void forEachConcurrently(final Function<? super T, ?> runnable, Executor executor) {
        Sequences.forEachConcurrently(this, runnable, executor);
    }

    default void each(final Function<? super T, ?> runnable) {
        forEach(runnable);
    }

    default void forEach(final Function<? super T, ?> runnable) {
        Sequences.forEach(this, runnable);
    }

    default <S> Seq<S> mapConcurrently(final Function<? super T, S> callable) {
        return Sequences.mapConcurrently(this, callable);
    }

    default <S> Seq<S> mapConcurrently(final Function<? super T, S> callable, final Executor executor) {
        return Sequences.mapConcurrently(this, callable, executor);
    }

    @Override
    default <S> Seq<S> map(final Function<? super T, ? extends S> callable) {
        return Sequences.map(this, callable);
    }

    default Pair<Seq<T>, Seq<T>> partition(final Predicate<? super T> predicate) {
        return Sequences.partition(this, predicate);
    }

    @Override
    default Seq<T> filter(final Predicate<? super T> predicate) {
        return Sequences.filter(this, predicate);
    }

    default <S> Seq<S> flatMap(final Function<? super T, ? extends Iterable<? extends S>> callable) {
        return Sequences.flatMap(this, callable);
    }

    default <S> Seq<S> flatMapConcurrently(final Function<? super T, ? extends Iterable<? extends S>> callable) {
        return Sequences.flatMapConcurrently(this, callable);
    }

    default <S> Seq<S> flatMapConcurrently(final Function<? super T, ? extends Iterable<? extends S>> callable, final Executor executor) {
        return Sequences.flatMapConcurrently(this, callable, executor);
    }

    default <B> Seq<B> applicate(final Seq<? extends Function<? super T, ? extends B>> applicator) {
        return Sequences.applicate(applicator, this);
    }

    default T first() {
        return Sequences.first(this);
    }

    default T last() {
        return Sequences.last(this);
    }

    default Option<T> lastOption() {
        return Sequences.lastOption(this);
    }

    default T second() {
        return Sequences.second(this);
    }

    @Override
    default T third() {
        return Sequences.third(this);
    }

    default T head() {
        return Sequences.head(this);
    }

    default Option<T> headOption() {
        return Sequences.headOption(this);
    }

    default Seq<T> tail() {
        return Sequences.tail(this);
    }

    default Seq<T> init() {
        return Sequences.init(this);
    }

    default <S> S fold(final S seed, final Function2<? super S, ? super T, ? extends S> callable) {
        return Sequences.fold(this, seed, callable);
    }

    default <S> S foldLeft(final S seed, final Function2<? super S, ? super T, ? extends S> callable) {
        return Sequences.foldLeft(this, seed, callable);
    }

    default <S> S foldRight(final S seed, final Function2<? super T, ? super S, ? extends S> callable) {
        return Sequences.foldRight(this, seed, callable);
    }

    default <S> S foldRight(final S seed, final Function<? super Pair<T, S>, ? extends S> callable) {
        return Sequences.foldRight(this, seed, callable);
    }

    default <S> S reduce(final Function2<? super S, ? super T, ? extends S> callable) {
        return Sequences.reduce(this, callable);
    }

    default <S> S reduceLeft(final Function2<? super S, ? super T, ? extends S> callable) {
        return Sequences.reduceLeft(this, callable);
    }

    default <S> S reduceRight(final Function2<? super T, ? super S, ? extends S> callable) {
        return Sequences.reduceRight(this, callable);
    }

    default <S> S reduceRight(final Function<? super Pair<T, S>, ? extends S> callable) {
        return Sequences.reduceRight(this, callable);
    }

    default String toString(final String separator) {
        return Sequences.toString(this, separator);
    }

    default String toString(final String start, final String separator, final String end) {
        return Sequences.toString(this, start, separator, end);
    }

    default <A extends Appendable> A appendTo(A appendable) {
        return Sequences.appendTo(this, appendable);
    }

    default <A extends Appendable> A appendTo(A appendable, final String separator) {
        return Sequences.appendTo(this, appendable, separator);
    }

    default <A extends Appendable> A appendTo(A appendable, final String start, final String separator, final String end) {
        return Sequences.appendTo(this, appendable, start, separator, end);
    }

    default Set<T> union(final Iterable<? extends T> other) {
        return Sets.union(toSet(), Sets.set(other));
    }

    default Set<T> intersection(final Iterable<? extends T> other) {
        return Sets.intersection(sequence(toSet(), Sets.set(other)));
    }

    default <S extends Set<T>> S toSet(S set) {
        return Sets.set(set, this);
    }

    default Set<T> toSet() {
        return toSet(new LinkedHashSet<>());
    }

    default Seq<T> unique() {
        return unique(returnArgument());
    }

    default <S> Seq<T> unique(Function<? super T, ? extends S> callable) {
        return Sequences.unique(this, callable);
    }

    @Override
    default Seq<T> empty() {
        return Sequences.empty();
    }

    default boolean isEmpty() {
        return Sequences.isEmpty(this);
    }

    default List<T> toList() {
        return Sequences.toList(this);
    }

    default List<T> toSortedList(Comparator<T> comparator) {
        return Sequences.toSortedList(this, comparator);
    }

    default Deque<T> toDeque() {
        return Sequences.toDeque(this);
    }

    @Override
    default Seq<T> delete(final T t) {
        return Sequences.delete(this, t);
    }

    @Override
    default Seq<T> deleteAll(final Iterable<? extends T> iterable) {
        return Sequences.deleteAll(this, iterable);
    }

    default int size() {
        return Sequences.size(this);
    }

    default Number number() {
        return Sequences.number(this);
    }

    default Seq<T> take(final int count) {
        return Sequences.take(this, count);
    }

    default Seq<T> takeWhile(final Predicate<? super T> predicate) {
        return Sequences.takeWhile(this, predicate);
    }

    default Seq<T> drop(final int count) {
        return Sequences.drop(this, count);
    }

    default Seq<T> dropWhile(final Predicate<? super T> predicate) {
        return Sequences.dropWhile(this, predicate);
    }

    default boolean forAll(final Predicate<? super T> predicate) {
        return Sequences.forAll(this, predicate);
    }

    @Override
    default boolean containsAll(Collection<?> c) {
        return forAll(in(c));
    }

    @Override
    default boolean contains(Object o) {
        return exists(is(o));
    }

    default boolean exists(final Predicate<? super T> predicate) {
        return Sequences.exists(this, predicate);
    }

    default Option<T> find(final Predicate<? super T> predicate) {
        return Sequences.find(this, predicate);
    }

    default <S> Option<S> tryPick(final Function<? super T, ? extends Option<? extends S>> callable) {
        return Sequences.tryPick(this, callable);
    }

    default <S> S pick(final Function<? super T, ? extends Option<? extends S>> callable) {
        return Sequences.pick(this, callable);
    }

    default Seq<T> append(final T t) {
        return Sequences.append(this, t);
    }

    default Seq<T> join(final Iterable<? extends T> iterable) {
        return Sequences.join(this, iterable);
    }

    @Override
    default <C extends Segment<T>> C joinTo(C rest) {
        return Sequences.joinTo(this, rest);
    }

    default Seq<T> cons(final T t) {
        return Sequences.cons(t, this);
    }

    default Seq<T> memoize() {
        return memorise();
    }

    default Seq<T> memorise() {
        return Sequences.memorise(this);
    }

    default ForwardOnlySequence<T> forwardOnly() {
        return Sequences.forwardOnly(this);
    }

    default <S> Seq<Pair<T, S>> zip(final Iterable<? extends S> second) {
        return Sequences.zip(this, second);
    }

    @SuppressWarnings("unchecked")
    default Seq<Seq<T>> transpose(final Iterable<? extends T>... iterables) {
        return transpose(sequence(iterables));
    }

    default Seq<Seq<T>> transpose(final Iterable<? extends Iterable<? extends T>> iterables) {
        return Sequences.transpose(Sequences.cons(this, sequence(iterables).<Iterable<T>>unsafeCast()));
    }

    default <S, Th> Seq<Triple<T, S, Th>> zip(final Iterable<? extends S> second, final Iterable<? extends Th> third) {
        return Sequences.zip(this, second, third);
    }

    default <S, Th, Fo> Seq<Quadruple<T, S, Th, Fo>> zip(final Iterable<? extends S> second, final Iterable<? extends Th> third, final Iterable<? extends Fo> fourth) {
        return Sequences.zip(this, second, third, fourth);
    }

    default <S, Th, Fo, Fi> Seq<Quintuple<T, S, Th, Fo, Fi>> zip(final Iterable<? extends S> second, final Iterable<? extends Th> third, final Iterable<? extends Fo> fourth, final Iterable<? extends Fi> fifth) {
        return Sequences.zip(this, second, third, fourth, fifth);
    }

    default Seq<Pair<Number, T>> zipWithIndex() {
        return Sequences.zipWithIndex(this);
    }

    default <R extends Comparable<? super R>> Seq<T> sortBy(final Function<? super T, ? extends R> callable) {
        return sortBy(ascending(callable));
    }

    default Seq<T> sort(final Comparator<? super T> comparator) {
        return sortBy(comparator);
    }

    default Seq<T> sortBy(final Comparator<? super T> comparator) {
        return Sequences.sortBy(this, comparator);
    }

    default <S> Seq<S> safeCast(final Class<? extends S> aClass) {
        return Sequences.safeCast(this, aClass);
    }

    default <S> Seq<S> unsafeCast() {
        return Sequences.unsafeCast(this);
    }

    default Seq<T> realise() {
        return Sequences.realise(this);
    }

    default Seq<T> reverse() {
        return Sequences.reverse(this);
    }

    default Seq<T> cycle() {
        return Sequences.cycle(this);
    }

    default <K> Map<K, List<T>> toMap(final Function<? super T, ? extends K> callable) {
        return Maps.multiMap(this, callable);
    }

    default <K> Seq<Group<K, T>> groupBy(final Function<? super T, ? extends K> callable) {
        return Sequences.groupBy(this, callable);
    }

    default Seq<Seq<T>> recursive(final Function<Seq<T>, Pair<Seq<T>, Seq<T>>> callable) {
        return Sequences.recursive(this, callable);
    }

    default Pair<Seq<T>, Seq<T>> splitAt(final Number index) {
        return Sequences.splitAt(this, index);
    }

    default Pair<Seq<T>, Seq<T>> splitWhen(final Predicate<? super T> predicate) {
        return Sequences.splitWhen(this, predicate);
    }

    default Pair<Seq<T>, Seq<T>> splitOn(final T instance) {
        return Sequences.splitOn(this, instance);
    }

    default Pair<Seq<T>, Seq<T>> span(final Predicate<? super T> predicate) {
        return Sequences.span(this, predicate);
    }

    default Pair<Seq<T>, Seq<T>> breakOn(final Predicate<? super T> predicate) {
        return Sequences.breakOn(this, predicate);
    }

    default Seq<T> shuffle() {
        return Sequences.shuffle(this);
    }

    default Seq<T> interruptable() {
        return Sequences.interruptable(this);
    }

    default PersistentList<T> toPersistentList() {
        return PersistentList.constructors.list(this);
    }

    default Seq<Pair<T, T>> cartesianProduct() {
        return Sequences.cartesianProduct(this);
    }

    default <S> Seq<Pair<T, S>> cartesianProduct(final Iterable<? extends S> other) {
        return Sequences.cartesianProduct(this, other);
    }

    default T get(int index) {
        return drop(index).head();
    }

    default Seq<Seq<T>> windowed(int size) {
        return Sequences.windowed(this, size);
    }

    default Seq<T> intersperse(T separator) {
        return Sequences.intersperse(this, separator);
    }

    default Option<Seq<T>> flatOption() {
        return Sequences.flatOption(this);
    }

    @Override
    default int indexOf(Object t) {
        return Sequences.indexOf(this, t);
    }

    default Seq<Seq<T>> grouped(int size) {
        return recursive(Sequences.<T>splitAt(size));
    }

    public static class functions {
        public static <T> Unary<Seq<T>> tail() {
            return Segment.functions.<T, Seq<T>>tail();
        }

        public static <T> Unary<Seq<T>> tail(Class<T> aClass) {
            return tail();
        }

        public static <T> Function2<Iterable<? extends T>, Iterable<? extends T>, Seq<T>> join() {
            return (a, b) -> sequence(Iterators.functions.<T>join().call(a, b));
        }
    }
}