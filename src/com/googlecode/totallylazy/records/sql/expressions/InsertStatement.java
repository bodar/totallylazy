package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;

import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.records.sql.expressions.Expressions.expression;
import static com.googlecode.totallylazy.records.sql.expressions.Expressions.textOnly;

public class InsertStatement extends CompoundExpression {
    public static final TextOnlyExpression INSERT = textOnly("insert");
    public static final TextOnlyExpression VALUES = textOnly("values");

    public InsertStatement(final Keyword recordName, final Record record) {
        super(
                INSERT,
                textOnly("into").join(textOnly(recordName)),
                columns(record),
                VALUES,
                values(record)
        );
    }

    public static TextOnlyExpression columns(Record record) {
        return textOnly(formatList(record.keywords()));
    }

    public static AbstractExpression values(Record record) {
        return expression(formatList(repeat("?").take((Integer) record.fields().size())),
                record.getValuesFor(record.keywords()));
    }

    public static String formatList(final Sequence<?> values) {
        return values.toString("(", ",", ")");
    }

    public static Function1<Record, Expression> insertStatement(final Keyword recordName) {
        return new Function1<Record, Expression>() {
            public Expression call(Record record) throws Exception {
                return insertStatement(recordName, record);
            }
        };
    }

    public static InsertStatement insertStatement(final Keyword recordName, final Record record) {
        return new InsertStatement(recordName, record);
    }
}
