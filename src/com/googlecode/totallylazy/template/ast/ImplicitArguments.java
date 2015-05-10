package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Value;

import java.util.List;

public class ImplicitArguments extends Value.Type<List<Expression>> implements Arguments<Number> {
    private ImplicitArguments(List<Expression> value) {
        super(value);
    }

    public static ImplicitArguments implicitArguments(List<Expression> value) {return new ImplicitArguments(value);}
}
