package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Value;

import java.util.Map;

public class NamedArguments extends Value.Type<Map<String, Expression>> implements Arguments<String> {
    private NamedArguments(Map<String, Expression> value) { super(value); }

    protected static NamedArguments namedArguments(Map<String, Expression> value) {return new NamedArguments(value);}
}
