package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Value;

public class Indirection extends Value.Type<Expression> implements Expression {
    private Indirection(Expression value) {
        super(value);
    }

    public static Indirection indirection(Expression value) {return new Indirection(value);}
}
