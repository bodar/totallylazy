package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;

import static com.googlecode.totallylazy.Sequences.repeat;
import static java.lang.String.format;

public class InsertStatement extends TextAndParametersExpression {
    public InsertStatement(final Keyword recordName, final Record record) {
        super(
                format("insert into %s (%s) values (%s)", recordName, record.keywords(), repeat("?").take((Integer) record.fields().size())),
                record.getValuesFor(record.keywords())
        );
    }

    public static Callable1<Record, Expression> insertStatement(final Keyword recordName) {
        return new Callable1<Record, Expression>() {
            public Expression call(Record record) throws Exception {
                return insertStatement(recordName, record);
            }
        };
    }

    public static InsertStatement insertStatement(final Keyword recordName, final Record record) {
        return new InsertStatement(recordName, record);
    }
}
