package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Objects;
import com.googlecode.totallylazy.Value;
import com.googlecode.totallylazy.annotations.multimethod;

import java.util.List;

import static com.googlecode.totallylazy.Arrays.list;

public class ImplicitArguments implements Arguments<Number>, Value<List<Expression>> {
    private final List<Expression> value;

    private ImplicitArguments(List<Expression> value) {
        this.value = value;
    }

    public static ImplicitArguments implicitArguments(Expression... value) {return new ImplicitArguments(list(value));}

    public static ImplicitArguments implicitArguments(List<Expression> value) {return new ImplicitArguments(value);}

    @Override
    public List<Expression> value() {
        return value;
    }

    @multimethod
    @Override
    public boolean equals(Object other) {
        return other instanceof ImplicitArguments && Objects.equalTo(value, ((ImplicitArguments) other).value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
