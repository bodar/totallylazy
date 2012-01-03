package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Aggregate;
import com.googlecode.totallylazy.records.AliasedKeyword;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.SelectCallable;

import static com.googlecode.totallylazy.records.sql.expressions.Expressions.expression;
import static com.googlecode.totallylazy.records.sql.expressions.Expressions.textOnly;
import static com.googlecode.totallylazy.records.sql.expressions.SetFunctionType.setFunctionType;

public class SelectList extends CompoundExpression{
    public SelectList(Sequence<Keyword> select) {
        super(select.map(derivedColumn()));
    }

    public static SelectList selectList(final Sequence<Keyword> select) {
        return new SelectList(select);
    }

    @Override
    public String text() {
        return expressions.map(Expressions.text()).toString();
    }

    @SuppressWarnings("unchecked")
    public static Callable1<Keyword, Expression> derivedColumn() {
        return new Callable1<Keyword, Expression>() {
            public Expression call(Keyword keyword) throws Exception {
                return derivedColumn(keyword);
            }
        };
    }

    public static <T> AbstractExpression derivedColumn(Callable1<? super Record, T> callable) {
        if(callable instanceof Aggregate){
            Aggregate aggregate = (Aggregate) callable;
            return setFunctionType(aggregate.callable(), aggregate.source()).join(asClause(aggregate));
        }
        if (callable instanceof AliasedKeyword) {
            AliasedKeyword aliasedKeyword = (AliasedKeyword) callable;
            return textOnly(aliasedKeyword.source()).join(asClause(aliasedKeyword));
        }
        if (callable instanceof Keyword) {
            Keyword keyword = (Keyword) callable;
            return textOnly(keyword);
        }
        if (callable instanceof SelectCallable) {
            Sequence<Keyword> keywords = ((SelectCallable) callable).keywords();
            return selectList(keywords);
        }
        throw new UnsupportedOperationException("Unsupported callable " + callable);
    }

    public static Expression asClause(Keyword keyword) {
        return expression("as " + keyword.name());
    }


}
