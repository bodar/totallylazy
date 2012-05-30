package com.googlecode.totallylazy.comparators;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.util.Comparator;

import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;

public class Comparators {
    public static <T, R> Comparator<T> where(final Callable1<? super T, ? extends R> callable, final Comparator<? super R> comparator) {
        return new Comparator<T>() {
            public int compare(T instance, T otherInstance) {
                return comparator.compare(call(callable, instance), call(callable, otherInstance));
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static final Comparator<Comparable> ASCENDING = new Comparator<Comparable>() {
        public int compare(Comparable a, Comparable b) {
            return a.compareTo(b);
        }
    };

    public static <T extends Comparable<? super T>> Comparator<T> ascending() {
        return cast(ASCENDING);
    }

    public static <T extends Comparable<? super T>> Comparator<T> ascending(Class<T> aClass) {
        return Comparators.<T>ascending();
    }

    @SuppressWarnings("unchecked")
    private static final Comparator<Comparable> DESCENDING = new Comparator<Comparable>() {
        public int compare(Comparable a, Comparable b) {
            return b.compareTo(a);
        }
    };

    public static <T extends Comparable<? super T>> Comparator<T> descending() {
        return cast(DESCENDING);
    }

    public static <T extends Comparable<? super T>> Comparator<T> descending(Class<T> aClass) {
        return Comparators.<T>descending();
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

    public static <T> Comparator<T> comparators(final Comparator<? super T>  first) {
        return comparators(Sequences.<Comparator<? super T>>sequence(first));
    }

    public static <T> Comparator<T> comparators(final Comparator<? super T>  first, final Comparator<? super T>  second) {
        return comparators(Sequences.<Comparator<? super T>>sequence(first, second));
    }

    public static <T> Comparator<T> comparators(final Comparator<? super T>  first, final Comparator<? super T>  second, final Comparator<? super T> third) {
        return comparators(Sequences.<Comparator<? super T>>sequence(first, second, third));
    }

    public static <T> Comparator<T> comparators(final Comparator<? super T>  first, final Comparator<? super T>  second, final Comparator<? super T> third, final Comparator<? super T> fourth) {
        return comparators(Sequences.<Comparator<? super T>>sequence(first, second, third, fourth));
    }

    public static <T> Comparator<T> comparators(final Comparator<? super T>  first, final Comparator<? super T>  second, final Comparator<? super T> third, final Comparator<? super T> fourth, final Comparator<? super T>  fifth) {
        return comparators(Sequences.<Comparator<? super T>>sequence(first, second, third, fourth, fifth));
    }

}
