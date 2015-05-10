package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Value;

public class Name extends Value.Type<String> implements Expression {
    private Name(String value) { super(value); }

    public static Name name(String value) {return new Name(value);}
}
