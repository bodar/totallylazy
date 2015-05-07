package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.template.ast.AnonymousTemplate;
import com.googlecode.totallylazy.template.ast.Attribute;
import com.googlecode.totallylazy.template.ast.FunctionCall;
import com.googlecode.totallylazy.template.ast.Grammar;
import com.googlecode.totallylazy.template.ast.Mapping;

import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Maps.mapValues;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Template implements Renderer<Map<String, Object>> {
    private final List<Object> template;
    private final TemplateGroup parent;

    private Template(List<Object> template, TemplateGroup parent) {
        this.template = template;
        this.parent = parent;
    }

    public static Template template(String template) {
        return template(template, EmptyTemplateGroup.Instance);}
    public static Template template(String template, TemplateGroup parent) {
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
        if(expression instanceof Mapping){
            Mapping mapping = (Mapping) expression;
            Object value = context.get(mapping.attribute().value());
            AnonymousTemplate anonymousTemplate = (AnonymousTemplate) mapping.expression();
            Template template = new Template(anonymousTemplate.template(), parent);
            if(value instanceof Iterable) {
                return sequence((Iterable<?>) value).zipWithIndex().fold(appendable, (a, p) -> {
                    List<String> params = anonymousTemplate.paramaeterNames();
                    if(params.size() == 1) params.add("index");
                    return template.render(map(params.get(0), p.second(), params.get(1), p.first()), a);
                });
            }
        }
        throw new IllegalArgumentException("Unknown expression type: " + expression);
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
