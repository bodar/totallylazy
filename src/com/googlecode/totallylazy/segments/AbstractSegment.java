package com.googlecode.totallylazy.segments;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.iterators.SegmentIterator;

import java.util.Iterator;

import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Unchecked.cast;

public abstract class AbstractSegment<T> implements Segment<T>, Iterable<T> {
    @Override
    public Option<T> headOption() {
        return isEmpty()
                ? Option.<T>none()
                : some(head());
    }

    @Override
    public String toString() {
        return sequence().toString();
    }

    @Override
    public Iterator<T> iterator() {
        return SegmentIterator.iterator(this);
    }

    public Sequence<T> sequence() {
        return methods.sequence(this);
    }

    @Override
    public int hashCode() {
        return sequence().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Segment && methods.equalTo((Segment<?>) obj, this);
    }
}
