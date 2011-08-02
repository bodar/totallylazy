package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequences;

import static com.googlecode.totallylazy.Predicates.and;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;

public class Using implements Callable1<Record, Predicate<? super Record>> {
    private final Keyword<?>[] keywords;

    public Using(Keyword<?>[] keywords) {
        this.keywords = keywords;
    }

    public Predicate<? super Record> call(Record record) {
        return and(Sequences.sequence(keywords).map(asPredicate(record)).toArray(Predicate.class));
    }

    private Callable1<? super Keyword<?>,Predicate> asPredicate(final Record record) {
        return new Callable1<Keyword<?>, Predicate>() {
            public Predicate call(Keyword<?> keyword) throws Exception {
                return where(keyword, is(record.get(keyword)));
            }
        };
    }

    public Keyword<?>[] keywords() {
        return keywords;
    }
}
