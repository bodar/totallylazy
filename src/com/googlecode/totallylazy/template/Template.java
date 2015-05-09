package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.template.ast.Anonymous;
import com.googlecode.totallylazy.template.ast.Attribute;
import com.googlecode.totallylazy.template.ast.Expression;
import com.googlecode.totallylazy.template.ast.FunctionCall;
import com.googlecode.totallylazy.template.ast.Grammar;
import com.googlecode.totallylazy.template.ast.Indirection;
import com.googlecode.totallylazy.template.ast.Mapping;
import com.googlecode.totallylazy.template.ast.Text;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Maps.mapValues;
import static com.googlecode.totallylazy.Sequences.one;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.string;
import static com.googlecode.totallylazy.Unchecked.cast;

public class Template implements Renderer<Map<String, Object>> {
    private final List<Expression> template;
    private final Renderers parent;

    private Template(List<Expression> template, Renderers parent) {
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

    Appendable append(Expression expression, Map<String, Object> context, Appendable appendable) throws Exception {
        if(expression instanceof Text) return appendable.append(((Text) expression).charSequence());
        if(expression instanceof Attribute) return parent.render(value((Attribute) expression, context), appendable);
        if(expression instanceof FunctionCall) return append((FunctionCall) expression, context, appendable);
        if(expression instanceof Indirection) return append(((Indirection) expression).expression(), context, appendable);
        if(expression instanceof Anonymous) return append((Anonymous) expression, context, appendable);
        if(expression instanceof Mapping) return append((Mapping) expression, context, appendable);
        throw new IllegalArgumentException("Unknown expression type: " + expression);
    }

    Appendable append(FunctionCall functionCall, Map<String, Object> context, Appendable appendable) throws Exception {
        return parent.get(name(functionCall.name(), context)).render(value((Object) functionCall.arguments(), context), appendable);
    }

    Appendable append(Anonymous anonymous, Map<String, Object> context, Appendable appendable) throws Exception {
        return new Template(anonymous.template(), parent).render(context, appendable);
    }

    Appendable append(Mapping mapping, Map<String, Object> context, Appendable appendable) throws Exception {Anonymous anonymous = mapping.expression();
        Object value = value(mapping.attribute(), context);
        if(value instanceof Map) return appendPairs(anonymous, Maps.pairs(cast(value)), appendable);
        if(value instanceof Iterable) return appendIterable(anonymous, (Iterable<?>) value, appendable);
        return appendIterable(anonymous, one(value), appendable);
    }

    Appendable appendIterable(Anonymous anonymous, Iterable<?> values, Appendable appendable) throws Exception {
        return appendPairs(anonymous, sequence(values).zipWithIndex(), appendable);
    }

    Appendable appendPairs(Anonymous anonymous, Iterable<? extends Pair<?, ?>> pairs, Appendable appendable) throws Exception {
        return sequence(pairs).fold(appendable, (a, p) ->
                append(anonymous,
                map(sequence(anonymous.paramaeterNames()).zip(sequence(p.second(), p.first()))), a));
    }

    String name(Object value, Map<String, Object> context) throws Exception {
        return string(value(value, context));
    }

    Object value(Object value, Map<String, Object> context) throws Exception {
        if(value instanceof CharSequence) return value;
        if(value instanceof Text) return ((Text)value).charSequence();
        if(value instanceof Attribute) return value((Attribute) value, context);
        if(value instanceof FunctionCall) return value((FunctionCall) value, context);
        if(value instanceof Indirection) return value(((Indirection) value).expression(), context);
        if(value instanceof List) return value((List<?>) value, context);
        if(value instanceof Map) return mapValues((Map<?, ?>) value, n -> value(n, context));
        throw new IllegalArgumentException("Unknown value type: " + value);
    }

    String value(FunctionCall functionCall, Map<String, Object> context) throws Exception {
        return parent.get(name(functionCall.name(), context)).render(value((Object) functionCall.arguments(), context));
    }

    Object value(List<?> list, Map<String, Object> context) throws Exception {
        if(list.isEmpty()) return context;
        if(list.size() == 1) return value(list.get(0), context);
        return sequence(list).map(arg -> value(arg, context)).toList();
    }

    Object value(Attribute attribute, Map<String, Object> context) {
        return sequence(attribute.value()).fold(context, (Object container, Object name) -> {
            if(container instanceof Map) return ((Map<?,?>) container).get(name);
            return container;
        });
    }

    @Override
    public String toString() {
        return sequence(template).toString("");
    }
}
