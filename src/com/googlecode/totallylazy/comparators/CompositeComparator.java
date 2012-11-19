package com.googlecode.totallylazy.comparators;

import com.googlecode.totallylazy.Sequence;

import java.util.Comparator;

public class CompositeComparator<T> implements Comparator<T> {
    private final Sequence<Comparator<? super T>> comparators;

    public CompositeComparator(Sequence<Comparator<? super T>> comparators) {this.comparators = comparators;}

    public int compare(T m1, T m2) {
        for (Comparator<? super T> comparator : comparators) {
            int comparisonResult = comparator.compare(m1, m2);
            if (comparisonResult != 0) return comparisonResult;
        }
        return 0;
    }
}
