package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.template.ast.Anonymous;
import com.googlecode.totallylazy.template.ast.Attribute;
import com.googlecode.totallylazy.template.ast.Grammar;
import com.googlecode.totallylazy.template.ast.FunctionCall;
import com.googlecode.totallylazy.template.ast.Mapping;
import com.googlecode.totallylazy.template.ast.Text;
import org.junit.Test;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Lists.list;
import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.predicates.Predicates.instanceOf;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.template.ast.Attribute.attribute;
import static com.googlecode.totallylazy.template.ast.FunctionCall.functionCall;
import static com.googlecode.totallylazy.template.ast.ImplicitArguments.implicitArguments;
import static com.googlecode.totallylazy.template.ast.Indirection.indirection;
import static com.googlecode.totallylazy.template.ast.Name.name;
import static com.googlecode.totallylazy.template.ast.NamedArguments.namedArguments;
import static com.googlecode.totallylazy.template.ast.Text.text;

public class GrammarTest {
    @Test
    public void canParseSingleAttribute() throws Exception {
        Attribute attribute = Grammar.ATTRIBUTE.parse("foo").value();
        assertThat(attribute, is(attribute(name("foo"))));
    }

    @Test
    public void canParseAttributeWithSubAttribute() throws Exception {
        Attribute attribute = Grammar.ATTRIBUTE.parse("foo.bar").value();
        assertThat(attribute, is(attribute(name("foo"), name("bar"))));
    }

    @Test
    public void canParseText() throws Exception {
        Text text = Grammar.TEXT.parse("Some other text").value();
        assertThat(text, is(text("Some other text")));
    }

    @Test
    public void literalCanBeSingleOrDoubleQuoted() throws Exception {
        assertThat(Grammar.LITERAL.parse("\"foo\"").value(), is(text("foo")));
        assertThat(Grammar.LITERAL.parse("'foo'").value(), is(text("foo")));
    }

    @Test
    public void canParseAnExpression() throws Exception {
        assertThat(Grammar.EXPRESSION.parse("$template()$").value(), instanceOf(FunctionCall.class));
        assertThat(Grammar.EXPRESSION.parse("$template$").value(), instanceOf(Attribute.class));
    }

    @Test
    public void canParseFunctionCallWithNoParameters() throws Exception {
        FunctionCall unnamed = Grammar.FUNCTION_CALL.parse("template()").value();
        assertThat(unnamed, is(functionCall(name("template"), implicitArguments())));
    }

    @Test
    public void canParseFunctionCallWithNamedParameters() throws Exception {
        FunctionCall functionCall = Grammar.FUNCTION_CALL.parse("template(foo=bar, baz=dan)").value();
        assertThat(functionCall, is(functionCall(name("template"),
                namedArguments(map("foo", attribute(name("bar")), "baz", attribute(name("dan")))))));
    }

    @Test
    public void canParseFunctionCallImplicitParameters() throws Exception {
        FunctionCall functionCall = Grammar.FUNCTION_CALL.parse("template(foo, bar, baz)").value();
        assertThat(functionCall, is(functionCall(name("template"),
                implicitArguments(attribute(name("foo")), attribute(name("bar")), attribute(name("baz"))))));
    }

    @Test
    public void canParseFunctionCallLiteralParameters() throws Exception {
        FunctionCall functionCall = Grammar.FUNCTION_CALL.parse("template(\"foo\")").value();
        assertThat(functionCall, is(functionCall(name("template"), implicitArguments(text("foo")))));
    }

    @Test
    public void canParseImplicits() throws Exception {
        assertThat(Grammar.IMPLICIT_ARGUMENTS.parse("a").value(), is(implicitArguments(attribute(name("a")))));
        assertThat(Grammar.IMPLICIT_ARGUMENTS.parse("a,b").value(), is(implicitArguments(attribute(name("a")), attribute(name("b")))));
        assertThat(Grammar.IMPLICIT_ARGUMENTS.parse("\"a\"").value(), is(implicitArguments(text("a"))));
    }

    @Test
    public void canParseValue() throws Exception {
        assertThat(Grammar.VALUE.parse("a").value(), instanceOf(Attribute.class));
        assertThat(Grammar.VALUE.parse("\"a\"").value(), instanceOf(Text.class));
    }

    @Test
    public void canParseLiteral() throws Exception {
        Text text = Grammar.LITERAL.parse("\"Some other text\"").value();
        assertThat(text, is(text("Some other text")));
    }

    @Test
    public void canParseAnonymousTemplate() throws Exception {
        Anonymous template = Grammar.ANONYMOUS_TEMPLATE.parse("{ name | Hello $name$ }").value();
        assertThat(template.paramaeterNames(), is(list("name")));
    }

    @Test
    public void anonymousTemplateCanHaveNoArguments() throws Exception {
        Anonymous template = Grammar.ANONYMOUS_TEMPLATE.parse("{Hello $name$ }").value();
        assertThat(template.paramaeterNames(), is(list()));
    }

    @Test
    public void supportsMapping() throws Exception {
        Mapping mapping = Grammar.MAPPING.parse("users:{ user | Hello $user$ }").value();
        assertThat(mapping.attribute(), is(attribute(name("users"))));
        assertThat(mapping.expression(), is(instanceOf(Anonymous.class)));
    }

    @Test
    public void supportsIndirectionInFunctionCall() throws Exception {
        FunctionCall functionCall = Grammar.FUNCTION_CALL.parse("(template)()").value();
        assertThat(functionCall, is(functionCall(indirection(attribute(name("template"))), implicitArguments())));
    }

    @Test
    public void supportsIndirectionInAttribute() throws Exception {
        Attribute result = Grammar.ATTRIBUTE.parse("root.('parent').child").value();
        assertThat(result, is(attribute(name("root"), indirection(text("parent")), name("child"))));
    }
}