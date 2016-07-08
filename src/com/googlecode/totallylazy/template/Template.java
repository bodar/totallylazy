package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.parser.Parser;
import com.googlecode.totallylazy.template.Renderers.Empty;
import com.googlecode.totallylazy.template.ast.Anonymous;
import com.googlecode.totallylazy.template.ast.Arguments;
import com.googlecode.totallylazy.template.ast.Attribute;
import com.googlecode.totallylazy.template.ast.Expression;
import com.googlecode.totallylazy.template.ast.FunctionCall;
import com.googlecode.totallylazy.template.ast.Grammar;
import com.googlecode.totallylazy.template.ast.ImplicitArguments;
import com.googlecode.totallylazy.template.ast.Indirection;
import com.googlecode.totallylazy.template.ast.Mapping;
import com.googlecode.totallylazy.template.ast.Name;
import com.googlecode.totallylazy.template.ast.NamedArguments;
import com.googlecode.totallylazy.template.ast.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Maps.mapValues;
import static com.googlecode.totallylazy.Sequences.one;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.string;

public class Template extends Renderer<Map<String, Object>> {
    private final List<Expression> template;
    private final Renderers parent;

    private Template(List<Expression> template, Renderers parent) {
        this.template = template;
        this.parent = parent;
    }

    public static Template template(String template) {
        return template(template, Empty.Instance);
    }

    public static Template template(String template, Renderers parent) {
        return template(template, parent, Grammar.parser());
    }

    public static Template template(String template, Parser<List<Expression>> parser) {
        return new Template(parser.parse(template).value(), Empty.Instance);
    }

    public static Template template(String template, Renderers parent, Parser<List<Expression>> parser) {
        return new Template(parser.parse(template).value(), parent);
    }

    public static Template template(InputStream template) {
        return template(template, Empty.Instance);
    }

    public static Template template(InputStream template, Renderers parent) {
        return template(template, parent, Grammar.parser());
    }

    public static Template template(InputStream template, Parser<List<Expression>> parser) {
        return new Template(parser.parse(template).value(), Empty.Instance);
    }

    public static Template template(InputStream template, Renderers parent, Parser<List<Expression>> parser) {
        return new Template(parser.parse(template).value(), parent);
    }

    @Override
    public Appendable render(final Map<String, Object> context, Appendable appendable) throws IOException {
        return sequence(template).
                fold(appendable, new Callable2<Appendable, Expression, Appendable>() {
                    @Override
                    public Appendable call(Appendable appendable, Expression expression) throws Exception {
                        return append(expression, context, appendable);
                    }
                });
    }

    Appendable append(Expression expression, Map<String, Object> context, Appendable appendable) throws Exception {
        if (expression instanceof Text) return appendable.append(((Text) expression).value());
        if (expression instanceof Attribute) return append((Attribute) expression, context, appendable);
        if (expression instanceof FunctionCall) return append((FunctionCall) expression, context, appendable);
        if (expression instanceof Indirection) return append(((Indirection) expression).value(), context, appendable);
        if (expression instanceof Anonymous) return append((Anonymous) expression, context, appendable);
        if (expression instanceof Mapping) return append((Mapping) expression, context, appendable);
        throw new IllegalArgumentException("Unknown expression type: " + expression);
    }

    Appendable append(Attribute attribute, Map<String, Object> context, Appendable appendable) throws Exception {
        return parent.render(value(attribute, context), appendable);
    }

    Appendable append(FunctionCall functionCall, Map<String, Object> context, Appendable appendable) throws Exception {
        return parent.get(name(functionCall.name(), context)).render(arguments(functionCall.arguments(), context), appendable);
    }

    Appendable append(Anonymous anonymous, Map<String, Object> context, Appendable appendable) throws Exception {
        return new Template(anonymous.template(), parent).render(context, appendable);
    }

    Appendable append(Mapping mapping, Map<String, Object> context, Appendable appendable) throws Exception {
        Anonymous anonymous = mapping.expression();
        Object value = value(mapping.attribute(), context);
        if (value instanceof Map)
            return appendPairs(anonymous, Maps.pairs(Unchecked.<Map<Object, Object>>cast(value)), appendable);
        if (value instanceof Iterable) return appendIterable(anonymous, (Iterable<?>) value, appendable);
        return appendIterable(anonymous, one(value), appendable);
    }

    Appendable appendIterable(Anonymous anonymous, Iterable<?> values, Appendable appendable) throws Exception {
        return appendPairs(anonymous, sequence(values).zipWithIndex(), appendable);
    }

    Appendable appendPairs(final Anonymous anonymous, Iterable<? extends Pair<?, ?>> pairs, Appendable appendable) throws Exception {
        return sequence(pairs).fold(appendable, new Callable2<Appendable, Pair<?, ?>, Appendable>() {
            @Override
            public Appendable call(Appendable appendable, Pair<?, ?> pair) throws Exception {
                return append(anonymous,
                        map(sequence(anonymous.paramaeterNames()).zip(sequence(pair.second(), pair.first()))), appendable);
            }
        });
    }

    String name(Expression value, Map<String, Object> context) throws Exception {
        if (value instanceof Name) return ((Name) value).value();
        if (value instanceof Indirection) return string(value(((Indirection) value).value(), context));
        throw new IllegalArgumentException("Unknown name type: " + value);
    }

    Object value(Expression value, Map<String, Object> context) throws Exception {
        if (value instanceof Text) return ((Text) value).value();
        if (value instanceof Name) return context.get(name(value, context));
        if (value instanceof Indirection) return context.get(name(value, context));
        if (value instanceof Attribute) return value((Attribute) value, context);
        if (value instanceof FunctionCall) return value((FunctionCall) value, context);
        if (value instanceof Anonymous) return value((Anonymous) value, context);
        throw new IllegalArgumentException("Unknown value type: " + value);
    }

    String value(Anonymous anonymous, Map<String, Object> context) throws Exception {
        return new Template(anonymous.template(), parent).render(context);
    }

    String value(FunctionCall functionCall, Map<String, Object> context) throws Exception {
        return parent.get(name(functionCall.name(), context)).render(arguments(functionCall.arguments(), context));
    }

    Object value(Attribute attribute, Map<String, Object> context) {
        return sequence(attribute.value()).fold(context, new Callable2<Object, Expression, Object>() {
            @Override
            public Object call(Object container, Expression name) throws Exception {
                if (container instanceof Map) return value(name, Unchecked.<Map<String, Object>>cast(container));
                if (container == null) return null;
                throw new IllegalArgumentException("Unknown container type: " + container);
            }
        });
    }

    Object arguments(Arguments<?> arguments, final Map<String, Object> context) throws Exception {
        if (arguments instanceof ImplicitArguments) return arguments((ImplicitArguments) arguments, context);
        if (arguments instanceof NamedArguments)
            return mapValues(((NamedArguments) arguments).value(), new Callable1<Expression, Object>() {
                @Override
                public Object call(Expression expression) throws Exception {
                    return value(expression, context);
                }
            });
        throw new IllegalArgumentException("Unknown arguments type: " + arguments);
    }

    Object arguments(ImplicitArguments arguments, final Map<String, Object> context) throws Exception {
        List<Expression> list = arguments.value();
        if (list.isEmpty()) return context;
        if (list.size() == 1) return value(list.get(0), context);
        return sequence(list).map(new Callable1<Expression, Object>() {
            @Override
            public Object call(Expression expression) throws Exception {
                return value(expression, context);
            }
        }).toList();
    }

    @Override
    public String toString() {
        return sequence(template).toString("");
    }
}
