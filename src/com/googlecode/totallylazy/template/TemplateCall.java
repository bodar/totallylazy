package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Mapper;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.annotations.multimethod;
import com.googlecode.totallylazy.multi;

import java.util.Map;

import static com.googlecode.totallylazy.Callables.toString;
import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Maps.pairs;
import static java.lang.String.format;

public class TemplateCall implements Renderer<Map<String, Object>> {
    private final String name;
    private final Map<String, Renderer<Map<String, Object>>> argumentsExtractors;
    private final Funclate funclate;

    public TemplateCall(final String name, final Map<String, Renderer<Map<String, Object>>> argumentsExtractors, final Funclate funclate) {
        this.name = name;
        this.argumentsExtractors = argumentsExtractors;
        this.funclate = funclate;
    }

    public String name() {
        return name;
    }

    public Map<String, Renderer<Map<String, Object>>> arguments() {
        return argumentsExtractors;
    }

    @Override
    public <A extends Appendable> A render(Map<String, Object> context, A appendable) throws Exception {
        return funclate.get(name).render(args(argumentsExtractors, context), appendable);
    }

    private Map<String, String> args(Map<String, Renderer<Map<String, Object>>> arguments, Map<String, Object> context) {
        return map(pairs(arguments).map(Callables.<String, Renderer<Map<String, Object>>, String>second(r -> r.render(context))));
    }

    @Override
    public String toString() {
        return format("%s(%s)", name, pairs(argumentsExtractors).map(new Mapper<Pair<?, ?>, String>() {
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
