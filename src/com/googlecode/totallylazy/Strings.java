package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.ContainsPredicate;
import com.googlecode.totallylazy.predicates.EndsWithPredicate;
import com.googlecode.totallylazy.predicates.StartsWithPredicate;

public class Strings {
    public static Callable1<String, String> toLowerCase() {
        return new Callable1<String, String>() {
            public String call(String value) throws Exception {
                return value.toLowerCase();
            }
        };
    }

    public static Callable1<String, String> toUpperCase() {
        return new Callable1<String, String>() {
            public String call(String value) throws Exception {
                return value.toUpperCase();
            }
        };
    }

    public static Predicate<String> startsWith(final String value){
        return new StartsWithPredicate(value);
    }

    public static Predicate<String> endsWith(final String value){
        return new EndsWithPredicate(value);
    }

    public static Predicate<String> contains(final String value){
        return new ContainsPredicate(value);
    }

    public static Predicate<String> equalIgnoringCase(final String expected) {
        return new Predicate<String>() {
            public boolean matches(String actual) {
                return expected.equalsIgnoreCase(actual);
            }
        };
    }
}
