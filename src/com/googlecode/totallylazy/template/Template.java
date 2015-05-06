package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.template.ast.Attribute;
import com.googlecode.totallylazy.template.ast.Grammar;
import com.googlecode.totallylazy.template.ast.Node;
import com.googlecode.totallylazy.template.ast.TemplateCall;
import com.googlecode.totallylazy.template.ast.Text;

import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Maps.pairs;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;

public class Template implements Renderer<Map<String, Object>> {
    private final String template;
    private final TemplateGroup parent;

    private Template(String template, TemplateGroup parent) {
        this.parent = parent;
        this.template = template;
    }

    public static Template template(String template) {return template(template, TemplateGroup.defaultTemplateGroup());}
    public static Template template(String template, TemplateGroup parent) {return new Template(template, parent);}

    @Override
    public <A extends Appendable> A render(Map<String, Object> context, A appendable) throws Exception {
        return sequence(Grammar.TEMPLATE.parse(template).value()).
                fold(appendable, (a, node) -> append(node, context, a));
    }

    <A extends Appendable> A append(Node node, Map<String, Object> context, A appendable) throws Exception {
        if(node instanceof Text) return cast(appendable.append(((Text) node).value()));
        if(node instanceof Attribute) return parent.render(context.get(((Attribute) node).value()), appendable);
        if(node instanceof TemplateCall){
            TemplateCall templateCall = (TemplateCall) node;
            return parent.get(templateCall.name()).render(values(templateCall.arguments(), context), appendable);
        }
        return appendable;
    }

    private Map<String, Object> values(Map<String, Node> arguments, Map<String, Object> context) {
        if(arguments.isEmpty()) return context;
        return map(pairs(arguments).map(Callables.<String, Node, Object>second(n -> value(n, context))));
    }

    Object value(Node node, Map<String, Object> context) throws Exception {
        if(node instanceof Text) return ((Text) node).value();
        if(node instanceof Attribute) return context.get(((Attribute) node).value());
        if(node instanceof TemplateCall) {
            TemplateCall templateCall = (TemplateCall) node;
            return parent.get(templateCall.name()).render(values(templateCall.arguments(), context));
        }
        throw new IllegalArgumentException("Unknown Node Type");
    }

    @Override
    public String toString() {
        return template;
    }
}
