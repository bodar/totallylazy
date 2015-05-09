package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Value;

import java.util.Iterator;
import java.util.Map;

public class NamedArguments implements Arguments<String>, Value<Map<String, Expression>> {
    private final Map<String, Expression> arguments;

    public NamedArguments(Map<String, Expression> arguments) {
        this.arguments = arguments;
    }

    @Override
    public Map<String, Expression> value() {
        return arguments;
    }

    @Override
    public Iterator<Pair<String, Expression>> iterator() {
        return Maps.pairs(arguments).iterator();
    }
}
