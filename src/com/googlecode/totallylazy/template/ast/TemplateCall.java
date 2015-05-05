package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Mapper;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.annotations.multimethod;
import com.googlecode.totallylazy.multi;
import com.googlecode.totallylazy.template.Renderer;

import java.util.Map;

import static com.googlecode.totallylazy.Callables.toString;
import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Maps.pairs;
import static java.lang.String.format;

public class TemplateCall implements Node {
    private final String name;
    private final Map<String, Node> arguments;

    public TemplateCall(final String name, final Map<String, Node> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public String name() {
        return name;
    }

    public Map<String, Node> arguments() {
        return arguments;
    }


    @Override
    public String toString() {
        return format("%s(%s)", name, pairs(arguments).map(new Mapper<Pair<?, ?>, String>() {
            @Override
            public String call(Pair<?, ?> pair) throws Exception {
                return renderName(pair) + renderValue(pair.second());
            }

            private String renderName(Pair<?, ?> pair) {
                if (pair.first().toString().matches("\\d+")) return "";
                return pair.first() + "=";
            }
            private multi multi;
            private String renderValue(Object renderer) {
                if(multi == null) multi = new multi(){};
                return multi.<String>methodOption(renderer).getOrElse(toString.apply(renderer));
            }

            @multimethod String renderValue(Text text) { return String.format("\"%s\"", text); }

        }).toString(", "));
    }
}
