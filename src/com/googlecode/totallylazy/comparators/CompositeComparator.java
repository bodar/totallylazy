package com.googlecode.totallylazy.comparators;

import com.googlecode.totallylazy.Seq;

import java.util.Comparator;

import static com.googlecode.totallylazy.Sequences.sequence;

public class CompositeComparator<T> implements Comparator<T> {
    private final Seq<Comparator<? super T>> comparators;

    public CompositeComparator(Iterable<Comparator<? super T>> comparators) {this.comparators = sequence(comparators);}

    public int compare(T m1, T m2) {
        for (Comparator<? super T> comparator : comparators) {
            int comparisonResult = comparator.compare(m1, m2);
            if (comparisonResult != 0) return comparisonResult;
        }
        return 0;
    }

    public Seq<Comparator<? super T>> comparators(){
        return comparators;
    }
}
