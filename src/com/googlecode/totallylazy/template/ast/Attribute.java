package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Objects;
import com.googlecode.totallylazy.Value;
import com.googlecode.totallylazy.annotations.multimethod;

import java.util.List;

import static com.googlecode.totallylazy.Arrays.list;


public class Attribute implements Value<List<Expression>>, Expression {
    private final List<Expression> value;

    private Attribute(List<Expression> value) {
        this.value = value;
    }

    public static Attribute attribute(Expression... value) {return new Attribute(list(value));}

    public static Attribute attribute(List<Expression> value) {return new Attribute(value);}

    @Override
    public List<Expression> value() {
        return value;
    }

    @multimethod
    @Override
    public boolean equals(Object other) {
        return other instanceof Attribute && Objects.equalTo(value, ((Attribute) other).value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

}
