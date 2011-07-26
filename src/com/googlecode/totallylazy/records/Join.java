package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import static com.googlecode.totallylazy.Predicates.and;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.records.RecordCallables.merge;

public class Join implements Callable1<Record, Iterable<Record>> {
    private final Sequence<Record> records;
    private final Callable1<Record, Predicate<? super Record>> using;

    public Join(Sequence<Record> records, Callable1<Record, Predicate<? super Record>> using) {
        this.records = records;
        this.using = using;
    }

    public Iterable<Record> call(Record record) throws Exception {
        return records.filter(using.call(record)).map(merge(record));
    }

    public static Callable1<? super Record,Iterable<Record>> join(final Sequence<Record> records,
                                                                  final Callable1<Record, Predicate<? super Record>> using) {
        return new Join(records, using);
    }

    public static Using using(Keyword<?>... keyword) {
        return new Using(keyword);
    }

    public static class Using implements Callable1<Record, Predicate<? super Record>>{
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

    }
}