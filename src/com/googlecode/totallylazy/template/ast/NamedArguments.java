package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Objects;
import com.googlecode.totallylazy.Value;
import com.googlecode.totallylazy.annotations.multimethod;

import java.util.Map;

public class NamedArguments implements Arguments<String>, Value<Map<String, Expression>> {
    private final Map<String, Expression> value;

    private NamedArguments(Map<String, Expression> value) { this.value = value; }

    public static NamedArguments namedArguments(Map<String, Expression> value) {return new NamedArguments(value);}

    @Override
    public Map<String, Expression> value() {
        return value;
    }

    @multimethod
    @Override
    public boolean equals(Object other) {
        return other instanceof NamedArguments && Objects.equalTo(value, ((NamedArguments) other).value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
