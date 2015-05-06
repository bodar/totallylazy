package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.template.ast.Attribute;
import com.googlecode.totallylazy.template.ast.Grammar;
import com.googlecode.totallylazy.template.ast.FunctionCall;

import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Maps.pairs;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Template implements Renderer<Map<String, Object>> {
    private final String template;
    private final TemplateGroup parent;

    private Template(String template, TemplateGroup parent) {
        this.parent = parent;
        this.template = template;
    }

    public static Template template(String template) {
        return template(template, EmptyTemplateGroup.Instance);}
    public static Template template(String template, TemplateGroup parent) {return new Template(template, parent);}

    @Override
    public Appendable render(Map<String, Object> context, Appendable appendable) throws Exception {
        return sequence(Grammar.TEMPLATE.parse(template).value()).
                fold(appendable, (a, node) -> append(node, context, a));
    }

    Appendable append(Object node, Map<String, Object> context, Appendable appendable) throws Exception {
        if(node instanceof CharSequence) return appendable.append(((CharSequence) node));
        if(node instanceof Attribute) return parent.render(context.get(((Attribute) node).value()), appendable);
        if(node instanceof FunctionCall){
            FunctionCall functionCall = (FunctionCall) node;
            return parent.get(functionCall.name()).render(values(functionCall.arguments(), context), appendable);
        }
        return appendable;
    }

    private Map<String, Object> values(Map<String, Object> arguments, Map<String, Object> context) {
        if(arguments.isEmpty()) return context;
        return map(pairs(arguments).map(Callables.<String, Object, Object>second(n -> value(n, context))));
    }

    Object value(Object node, Map<String, Object> context) throws Exception {
        if(node instanceof CharSequence) return node;
        if(node instanceof Attribute) return context.get(((Attribute) node).value());
        if(node instanceof FunctionCall) {
            FunctionCall functionCall = (FunctionCall) node;
            return parent.get(functionCall.name()).render(values(functionCall.arguments(), context));
        }
        throw new IllegalArgumentException("Unknown Node Type");
    }

    @Override
    public String toString() {
        return template;
    }
}
