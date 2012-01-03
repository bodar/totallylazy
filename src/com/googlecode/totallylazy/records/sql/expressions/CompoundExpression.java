package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Sequences.sequence;

public class CompoundExpression extends AbstractExpression {
    protected final Sequence<Expression> expressions;
    private final String start;
    private final String separator;
    private final String end;

    public CompoundExpression(final Expression... expressions) {
        this(sequence(expressions));
    }

    public CompoundExpression(final Sequence<Expression> expressions) {
        this(expressions, "", " ", "");
    }

    public CompoundExpression(final Sequence<Expression> expressions, final String start, final String separator, final String end) {
        this.expressions = expressions;
        this.start = start;
        this.separator = separator;
        this.end = end;
    }

    public String text() {
        return expressions.filter(not(instanceOf(EmptyExpression.class))).
                map(Expressions.text()).
                toString(start, separator, end, Integer.MAX_VALUE).trim();
    }

    public Sequence<Object> parameters() {
        return expressions.flatMap(Expressions.parameters());
    }

}
