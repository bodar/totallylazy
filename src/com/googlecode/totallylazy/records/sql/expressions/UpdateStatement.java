package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;

import static com.googlecode.totallylazy.records.sql.expressions.Expressions.expression;
import static com.googlecode.totallylazy.records.sql.expressions.Expressions.textOnly;
import static com.googlecode.totallylazy.records.sql.expressions.WhereClause.whereClause;

public class UpdateStatement extends CompoundExpression {
    public static final TextOnlyExpression UPDATE = textOnly("update");
    public static final TextOnlyExpression SET = textOnly("set");

    public UpdateStatement(Keyword recordName, Predicate<? super Record> predicate, Record record) {
        super(
                UPDATE.join(textOnly(recordName)),
                setClause(record),
                whereClause(predicate)
                );
    }

    public static CompoundExpression setClause(Record record) {
        return SET.join(expression(record.keywords().map(Strings.format("%s=?")).toString(), record.getValuesFor(record.keywords())));
    }

    public static Expression updateStatement(Keyword recordName, Predicate<? super Record> predicate, Record record) {
        return new UpdateStatement(recordName, predicate, record);
    }

    public static Callable1<Pair<? extends Predicate<? super Record>, Record>, Expression> updateStatement(final Keyword recordName) {
        return new Callable1<Pair<? extends Predicate<? super Record>, Record>, Expression>() {
            public Expression call(Pair<? extends Predicate<? super Record>, Record> recordPair) throws Exception {
                return updateStatement(recordName, recordPair.first(), recordPair.second());
            }
        };
    }
}
