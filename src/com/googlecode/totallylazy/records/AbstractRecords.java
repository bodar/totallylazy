package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Arrays.list;
import static com.googlecode.totallylazy.Predicates.all;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.numbers.Numbers.sum;
import static com.googlecode.totallylazy.records.RecordMethods.merge;

public abstract class AbstractRecords implements Records {
    private final Map<Keyword, List<Keyword<?>>> definitions = new HashMap<Keyword, List<Keyword<?>>>();

    public void define(Keyword recordName, Keyword<?>... fields) {
        definitions.put(recordName, list(fields));
    }

    public Sequence<Keyword> definitions(Keyword recordName) {
        if (!definitions.containsKey(recordName)) {
            return Sequences.empty();
        }
        return sequence((List) definitions.get(recordName));
    }

    // Only override if you Schema based technology like SQL
    public boolean exists(Keyword recordName) {
        return true;
    }

    public Number add(Keyword recordName, Record... records) {
        if (records.length == 0) return 0;
        return add(recordName, sequence(records));
    }

    public Number set(final Keyword recordName, Pair<? extends Predicate<? super Record>, Record>... records) {
        return set(recordName, sequence(records));
    }

    public Number set(final Keyword recordName, Sequence<Pair<? extends Predicate<? super Record>, Record>> records) {
        return records.map(update(recordName, false)).reduce(sum());
    }

    public Number put(final Keyword recordName, Pair<? extends Predicate<? super Record>, Record>... records) {
        return put(recordName, sequence(records));
    }

    public Number put(final Keyword recordName, Sequence<Pair<? extends Predicate<? super Record>, Record>> records) {
        return records.map(update(recordName, true)).reduce(sum());
    }

    private Callable1<Pair<? extends Predicate<? super Record>, Record>, Number> update(final Keyword recordName, final boolean add) {
        return new Callable1<Pair<? extends Predicate<? super Record>, Record>, Number>() {
            public Number call(Pair<? extends Predicate<? super Record>, Record> pair) throws Exception {
                Predicate<? super Record> predicate = pair.first();
                Sequence<Record> matched = get(recordName).filter(predicate).realise();
                if (add && matched.isEmpty()) {
                    add(recordName, pair.second());
                    return 1;
                }
                Sequence<Record> updated = matched.map(merge(pair.second()));
                Number count = remove(recordName, predicate);
                add(recordName, updated);
                return count;
            }
        };
    }

    public Number remove(Keyword recordName) {
        return remove(recordName, all(Record.class));
    }
}
