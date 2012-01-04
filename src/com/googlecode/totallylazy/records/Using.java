package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import static com.googlecode.totallylazy.Predicates.and;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;

public class Using implements Callable1<Record, Predicate<Record>> {
    private final Sequence<Keyword> keywords;

    public Using(Sequence<Keyword> keywords) {
        this.keywords = keywords;
    }

    public static Using using(Keyword<?>... keyword) {
        return new Using(Sequences.<Keyword>sequence(keyword));
    }

    public static Using using(Sequence<Keyword> keyword) {
        return new Using(keyword);
    }

    public Predicate<Record> call(Record record) {
        return and(keywords.map(asPredicate(record)).toArray(Predicate.class));
    }

    @SuppressWarnings("unchecked")
    private Function1<Keyword, Predicate<Record>> asPredicate(final Record record) {
        return new Function1<Keyword, Predicate<Record>>() {
            public Predicate<Record> call(Keyword keyword) throws Exception {
                return where(keyword, is(record.get(keyword)));
            }
        };
    }

    public Sequence<Keyword> keywords() {
        return keywords;
    }
}
