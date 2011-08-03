package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;

import static com.googlecode.totallylazy.records.sql.expressions.Expressions.expression;
import static com.googlecode.totallylazy.records.sql.expressions.Expressions.textOnly;
import static com.googlecode.totallylazy.records.sql.expressions.InsertStatement.*;
import static com.googlecode.totallylazy.records.sql.expressions.SelectExpression.SELECT;
import static com.googlecode.totallylazy.records.sql.expressions.UpdateStatement.UPDATE;
import static com.googlecode.totallylazy.records.sql.expressions.UpdateStatement.setClause;
import static com.googlecode.totallylazy.records.sql.expressions.WhereClause.whereClause;

public class MergeStatement extends CompoundExpression {
    public MergeStatement(Keyword recordName, Predicate<? super Record> predicate, Record record) {
        super(
                textOnly("merge into").join(textOnly(recordName)),
                textOnly("using ("),
                    SELECT.join(WhereClause.toSql(predicate)),
                textOnly(") on").join(WhereClause.toSql(predicate)),
                textOnly("when matched then"),
                    UPDATE.join(setClause(record)),
                textOnly("when not matched then"),
                    INSERT.join(columns(record)),
                    VALUES.join(values(record))
        );
    }

    public static Expression mergeStatement(Keyword recordName, Predicate<? super Record> predicate, Record record) {
        return new MergeStatement(recordName, predicate, record);
    }

    public static Callable1<Pair<? extends Predicate<? super Record>, Record>, Expression> mergeStatement(final Keyword recordName) {
        return new Callable1<Pair<? extends Predicate<? super Record>, Record>, Expression>() {
            public Expression call(Pair<? extends Predicate<? super Record>, Record> recordPair) throws Exception {
                return mergeStatement(recordName, recordPair.first(), recordPair.second());
            }
        };
    }
}
