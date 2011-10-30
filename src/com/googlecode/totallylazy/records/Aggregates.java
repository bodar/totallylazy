package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Value;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Aggregates implements Callable2<Record, Record, Record>, Value<Sequence<Aggregate>> {
    private final Sequence<Aggregate> aggregates;

    public Aggregates(final Sequence<Aggregate> aggregates) {
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

    public Sequence<Aggregate> value() {
        return aggregates;
    }

    public static Aggregates to(final Aggregate... aggregates) {
        return aggregates(sequence(aggregates));
    }

    public static Aggregates aggregates(final Sequence<Aggregate> sequence) {
        return new Aggregates(sequence);
    }


}
