package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;

import static com.googlecode.totallylazy.Sequences.repeat;
import static java.lang.String.format;

public class InsertStatement extends TextAndParametersExpression {
    public InsertStatement(final Keyword recordName, final Sequence<Keyword> fields, final Sequence<Object> values) {
        super(
                format("insert into %s (%s) values (%s)", recordName, fields, repeat("?").take((Integer) fields.size())),
                values
        );
    }

    public static Callable1<Record, Expression> insertStatement(final Keyword recordName, final Sequence<Keyword> fields) {
        return new Callable1<Record, Expression>() {
            public Expression call(Record record) throws Exception {
                return insertStatement(recordName, fields, record.getValuesFor(fields));
            }
        };
    }

    public static InsertStatement insertStatement(final Keyword recordName, final Sequence<Keyword> fields, final Sequence<Object> values) {
        return new InsertStatement(recordName, fields, values);
    }
}
