package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Arrays {
    public static <T> LogicalPredicate<T[]> exists(final Predicate<? super T> predicate) {
        return new LogicalPredicate<T[]>() {
            public boolean matches(T[] array) {
                return sequence(array).exists(predicate);
            }
        };
    }

    public static <T> LogicalPredicate<T[]> empty() {
        return new LogicalPredicate<T[]>() {
            public boolean matches(T[] array) {
                return array.length == 0;
            }
        };
    }

    public static <T> List<T> list(T... values) {
        return Lists.list(values);
    }

    public static <T> boolean containsIndex(T[] array, int index) {
        return index < array.length;
    }

    public static <T> T[] array(T ... values) {
        return values;
    }

    public static <T> boolean equalTo(Third<T> ts, Iterable<?> obj) {
        throw new UnsupportedOperationException();
    }

    public static <T> boolean isEmpty(T[] array) {return array.length == 0;}

    public static <T> T head(T[] array) {
        return array[0];
    }

    public static <T> T[] tail(T[] array) {
        return java.util.Arrays.copyOfRange(array, 1, array.length);
    }

    public static Character[] characters(String value) {
        Character[] result = new Character[value.length()];
        for (int i = 0; i < value.length(); i++) {
            result[i] = value.charAt(i);
        }
        return result;
    }
}
