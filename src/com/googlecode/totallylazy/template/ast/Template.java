package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.parser.Parser;
import com.googlecode.totallylazy.parser.Parsers;
import com.googlecode.totallylazy.template.Funclate;
import com.googlecode.totallylazy.template.Grammar;
import com.googlecode.totallylazy.template.Renderer;

import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Maps.pairs;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;

public class Template implements Renderer<Map<String, Object>> {
    private final Funclate funclate;
    private final String template;

    public Template(Funclate funclate, String template) {
        this.funclate = funclate;
        this.template = template;
    }

    @Override
    public <A extends Appendable> A render(Map<String, Object> context, A appendable) throws Exception {
        return sequence(Grammar.TEMPLATE.parse(template).value()).
                fold(appendable, (a, node) -> append(node, context, a));
    }

    <A extends Appendable> A append(Node node, Map<String, Object> context, A appendable) throws Exception {
        if(node instanceof Attribute) return funclate.render(context.get(((Attribute) node).value()), appendable);
        if(node instanceof Text) return cast(appendable.append(((Text)node).value()));
        if(node instanceof TemplateCall){
            TemplateCall templateCall = (TemplateCall) node;
            return funclate.get(templateCall.name()).render(values(templateCall.arguments(), context), appendable);
        }
        return appendable;
    }

    private Map<String, Object> values(Map<String, Node> arguments, Map<String, Object> context) {
        if(arguments.isEmpty()) return context;
        return map(pairs(arguments).map(Callables.<String, Node, Object>second(n -> value(n, context))));
    }

    Object value(Node node, Map<String, Object> context) throws Exception {
        if(node instanceof Attribute) return context.get(((Attribute) node).value());
        if(node instanceof Text) return ((Text) node).value();
        if(node instanceof TemplateCall) {
            TemplateCall templateCall = (TemplateCall) node;
            return funclate.get(templateCall.name()).render(values(templateCall.arguments(), context));
        }
        throw new IllegalArgumentException("Unknown Node Type");
    }

    @Override
    public String toString() {
        return template;
    }
}
