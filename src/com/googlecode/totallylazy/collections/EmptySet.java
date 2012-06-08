package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.None;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.iterators.EmptyIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Unchecked.cast;

public class EmptySet<A> implements ImmutableSet<A> {
    protected final Function1<A, ImmutableSet<A>> creator;

    protected EmptySet(Callable1<? super A, ? extends ImmutableSet<A>> creator) {
        this.creator = Function1.function(creator);
    }

    public static <A> EmptySet<A> emptySet(Callable1<? super A, ? extends ImmutableSet<A>> creator) {
        return new EmptySet<A>(creator);
    }

    @Override
    public ImmutableList<A> immutableList() {
        return ImmutableList.constructors.empty();
    }

    @Override
    public ImmutableSet<A> put(A value) {
        return cons(value);
    }

    @Override
    public None<A> find(Predicate<? super A> predicate) {
        return None.none();
    }

    @Override
    public ImmutableSet<A> filter(Predicate<? super A> predicate) {
        return this;
    }

    @Override
    public <NewV> ImmutableSet<NewV> map(Callable1<? super A, ? extends NewV> transformer) {
        return cast(this);
    }

    @Override
    public ImmutableSet<A> remove(A value) {
        return this;
    }

    @Override
    public Pair<ImmutableSet<A>, A>  removeMinimum() {
        throw new NoSuchElementException();
    }

    @Override
    public Pair<ImmutableSet<A>, A>  removeMaximum() {
        throw new NoSuchElementException();
    }

    @Override
    public <C extends Segment<A, C>> C joinTo(C rest) {
        return rest;
    }

    @Override
    public ImmutableSet<A> cons(A newValue) {
        return creator.apply(newValue);
    }

    @Override
    public boolean contains(A other) {
        return false;
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EmptySet;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public A head() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    @Override
    public ImmutableSet<A> tail() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    @Override
    public Iterator<A> iterator() {
        return new EmptyIterator<A>();
    }
}
