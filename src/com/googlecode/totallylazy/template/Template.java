package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.template.ast.AnonymousTemplate;
import com.googlecode.totallylazy.template.ast.Attribute;
import com.googlecode.totallylazy.template.ast.FunctionCall;
import com.googlecode.totallylazy.template.ast.Grammar;
import com.googlecode.totallylazy.template.ast.Mapping;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Maps.mapValues;
import static com.googlecode.totallylazy.Sequences.one;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;

public class Template implements Renderer<Map<String, Object>> {
    private final List<Object> template;
    private final Renderers parent;

    private Template(List<Object> template, Renderers parent) {
        this.template = template;
        this.parent = parent;
    }

    public static Template template(String template) {
        return template(template, Renderers.Empty.Instance);
    }
    public static Template template(String template, Renderers parent) {
        return new Template(Grammar.TEMPLATE.parse(template).value(), parent);
    }
    public static Template template(InputStream template) {
        return template(template, Renderers.Empty.Instance);
    }
    public static Template template(InputStream template, Renderers parent) {
        return new Template(Grammar.TEMPLATE.parse(template).value(), parent);
    }

    @Override
    public Appendable render(Map<String, Object> context, Appendable appendable) throws Exception {
        return sequence(template).
                fold(appendable, (a, node) -> append(node, context, a));
    }

    Appendable append(Object expression, Map<String, Object> context, Appendable appendable) throws Exception {
        if(expression instanceof CharSequence) return appendable.append(((CharSequence) expression));
        if(expression instanceof Attribute) return parent.render(context.get(((Attribute) expression).value()), appendable);
        if(expression instanceof FunctionCall){
            FunctionCall functionCall = (FunctionCall) expression;
            return parent.get(functionCall.name()).render(arguments(functionCall.arguments(), context), appendable);
        }
        if(expression instanceof AnonymousTemplate) {
            AnonymousTemplate anonymousTemplate = (AnonymousTemplate) expression;
            return new Template(anonymousTemplate.template(), parent).render(context, appendable);
        }
        if(expression instanceof Mapping){
            Mapping mapping = (Mapping) expression;
            AnonymousTemplate anonymousTemplate = (AnonymousTemplate) mapping.expression();
            Object value = context.get(mapping.attribute().value());
            if(value instanceof Map) return appendPairs(anonymousTemplate, Maps.pairs(cast(value)), appendable);
            if(value instanceof Iterable) return appendIterable(anonymousTemplate, (Iterable<?>) value, appendable);
            return appendIterable(anonymousTemplate, one(value), appendable);
        }
        throw new IllegalArgumentException("Unknown expression type: " + expression);
    }

    private Appendable appendIterable(AnonymousTemplate anonymousTemplate, Iterable<?> values, Appendable appendable) throws Exception {
        return appendPairs(anonymousTemplate, sequence(values).zipWithIndex(), appendable);
    }

    private Appendable appendPairs(AnonymousTemplate anonymousTemplate, Iterable<? extends Pair<?, ?>> pairs, Appendable appendable) throws Exception {
        return sequence(pairs).fold(appendable, (a, p) ->
                append(anonymousTemplate,
                        map(sequence(anonymousTemplate.paramaeterNames()).zip(sequence(p.second(), p.first()))), a));
    }

    private Object arguments(Object arguments, Map<String, Object> context) throws Exception {
        if(arguments instanceof List) {
            List<?> list = (List<?>) arguments;
            if(list.isEmpty()) return context;
            if(list.size() == 1) return value(list.get(0), context);
            return sequence(list).map(arg -> value(arg, context)).toList();
        }
        if(arguments instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) arguments;
            return mapValues(map, n -> value(n, context));
        }
        throw new IllegalArgumentException("Unknown arguments type: " + arguments);
    }

    Object value(Object value, Map<String, Object> context) throws Exception {
        if(value instanceof CharSequence) return value;
        if(value instanceof Attribute) return context.get(((Attribute) value).value());
        if(value instanceof FunctionCall) {
            FunctionCall functionCall = (FunctionCall) value;
            return parent.get(functionCall.name()).render(arguments(functionCall.arguments(), context));
        }
        throw new IllegalArgumentException("Unknown value type: " + value);
    }

    @Override
    public String toString() {
        return sequence(template).toString("");
    }
}
