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
        if (lastDirection.contains(Direction.next)) zipper = zipper.next();
        lastDirection = some(Direction.next);
        return zipper.value();
    }

    @Override
    public boolean hasPrevious() {
        return !zipper.isFirst();
    }

    @Override
    public T previous() {
        if (lastDirection.contains(Direction.previous)) zipper = zipper.previous();
        lastDirection = some(Direction.previous);
        return zipper.value();
    }

    @Override
    public int nextIndex() {
        return zipper.index() + (lastDirection.contains(Direction.next) ? 1 : 0);
    }

    @Override
    public int previousIndex() {
        return zipper.index() - (lastDirection.contains(Direction.next) ? 0 : 1);
    }
}
