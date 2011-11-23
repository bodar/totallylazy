package com.googlecode.totallylazy.comparators;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.util.Comparator;

import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Comparators {
    public static <T, R> Comparator<? super T> where(final Callable1<T, R> callable, final Comparator<R> comparator) {
        return new Comparator<T>() {
            public int compare(T instance, T otherInstance) {
                return comparator.compare(call(callable, instance), call(callable, otherInstance));
            }
        };
    }

    public static <T extends Comparable<? super T>> Comparator<? super T> ascending() {
        return new Comparator<T>() {
            public int compare(T a, T b) {
                return a.compareTo(b);
            }
        };
    }

    public static <T extends Comparable<? super T>> Comparator<? super T> descending() {
        return new Comparator<T>() {
            public int compare(T a, T b) {
                return b.compareTo(a);
            }
        };
    }

    public static <T> Comparator<T> comparators(final Comparator<? super T>... comparators) {
        return comparators(sequence(comparators));
    }

    public static <T> Comparator<T> comparators(final Sequence<Comparator<? super T>> comparators) {
        return new Comparator<T>() {
            public int compare(T m1, T m2) {
                for (Comparator<? super T> comparator : comparators) {
                    int comparisonResult = comparator.compare(m1, m2);
                    if (comparisonResult != 0) return comparisonResult;
                }
                return 0;
            }
        };
    }

    public static <T> Comparator<? super T> comparators(final Comparator<? super T>  first) {
        return comparators(Sequences.<Comparator<? super T>>sequence(first));
    }

    public static <T> Comparator<? super T> comparators(final Comparator<? super T>  first, final Comparator<? super T>  second) {
        return comparators(Sequences.<Comparator<? super T>>sequence(first, second));
    }

    public static <T> Comparator<? super T> comparators(final Comparator<? super T>  first, final Comparator<? super T>  second, final Comparator<? super T> third) {
        return comparators(Sequences.<Comparator<? super T>>sequence(first, second, third));
    }

    public static <T> Comparator<? super T> comparators(final Comparator<? super T>  first, final Comparator<? super T>  second, final Comparator<? super T> third, final Comparator<? super T> fourth) {
        return comparators(Sequences.<Comparator<? super T>>sequence(first, second, third, fourth));
    }

    public static <T> Comparator<? super T> comparators(final Comparator<? super T>  first, final Comparator<? super T>  second, final Comparator<? super T> third, final Comparator<? super T> fourth, final Comparator<? super T>  fifth) {
        return comparators(Sequences.<Comparator<? super T>>sequence(first, second, third, fourth, fifth));
    }

}
