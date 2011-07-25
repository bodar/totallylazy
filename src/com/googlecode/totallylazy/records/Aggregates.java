package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Value;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Aggregates implements Callable2<Record, Record, Record>, Value<Iterable<Aggregate>> {
    private final Iterable<Aggregate> aggregates;

    public Aggregates(final Iterable<Aggregate> aggregates) {
        this.aggregates = aggregates;
    }

    public Record call(final Record accumulator, final Record nextRecord) throws Exception {
        Record result = new MapRecord();
        for (Aggregate aggregate : aggregates) {
            result.set(aggregate, aggregate.call(accumulatorValue(accumulator, aggregate), nextRecord.get(aggregate.source())));
        }
        return result;
    }

    private Object accumulatorValue(Record record, Aggregate aggregate) {
        Object value = record.get(aggregate.source());
        if(value == null) {
            return record.get(aggregate);
        }
        return value;
    }

    public Iterable<Aggregate> value() {
        return aggregates;
    }

    @SuppressWarnings("unchecked")
    public static Callable2<? super Record, ? super Record, Record> to(final Aggregate... aggregates) {
        return aggregates(sequence(aggregates));
    }

    public static Aggregates aggregates(final Iterable<Aggregate> sequence) {
        return new Aggregates(sequence);
    }


}
