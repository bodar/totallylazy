package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Block;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.iterators.ReadOnlyListIterator;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;

public class ZipperListIterator<T> extends ReadOnlyListIterator<T> {
    private Zipper<T> zipper;
    private Option<Direction> lastDirection;

    public enum Direction {next, previous}

    public ZipperListIterator(Zipper<T> zipper) {
        this.zipper = zipper;
        lastDirection = none();
    }

    @Override
    public boolean hasNext() {
        return !zipper.isLast();
    }

    @Override
    public T next() {
        if (!hasNext()) throw new NoSuchElementException();
        if (lastDirection.contains(Direction.next)) zipper.nextOption().each(update);
        lastDirection = some(Direction.next);
        return zipper.value();
    }

    @Override
    public boolean hasPrevious() {
        return !zipper.isFirst();
    }

    @Override
    public T previous() {
        if (!hasPrevious()) throw new NoSuchElementException();
        if (lastDirection.contains(Direction.previous)) zipper.previousOption().each(update);
        lastDirection = some(Direction.previous);
        return zipper.value();
    }

    private final Block<Zipper<T>> update = new Block<Zipper<T>>() {
        @Override
        protected void execute(Zipper<T> z) throws Exception {
            zipper = z;
        }
    };

    @Override
    public int nextIndex() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int previousIndex() {
        throw new UnsupportedOperationException();
    }
}
