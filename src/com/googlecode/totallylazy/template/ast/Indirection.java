package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Objects;
import com.googlecode.totallylazy.Value;
import com.googlecode.totallylazy.annotations.multimethod;

public class Indirection implements Expression, Value<Expression> {
    private final Expression value;

    private Indirection(Expression value) {
        this.value = value;
    }

    public static Indirection indirection(Expression value) {return new Indirection(value);}

    @Override
    public Expression value() {
        return value;
    }

    @multimethod
    @Override
    public boolean equals(Object other) {
        return other instanceof Indirection && Objects.equalTo(value, ((Indirection) other).value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
