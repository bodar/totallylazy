package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.template.ast.Anonymous;
import com.googlecode.totallylazy.template.ast.Attribute;
import com.googlecode.totallylazy.template.ast.Expression;
import com.googlecode.totallylazy.template.ast.Grammar;
import com.googlecode.totallylazy.template.ast.FunctionCall;
import com.googlecode.totallylazy.template.ast.ImplicitArguments;
import com.googlecode.totallylazy.template.ast.Indirection;
import com.googlecode.totallylazy.template.ast.Mapping;
import com.googlecode.totallylazy.template.ast.Name;
import com.googlecode.totallylazy.template.ast.NamedArguments;
import com.googlecode.totallylazy.template.ast.Text;
import org.junit.Test;

import java.util.List;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Lists.list;
import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.template.ast.Attribute.attribute;
import static com.googlecode.totallylazy.template.ast.Name.name;
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
        assertThat(unnamed.name(), is(name("template")));
        ImplicitArguments arguments = (ImplicitArguments) unnamed.arguments();
        assertThat(arguments.value().isEmpty(), is(true));
    }

    @Test
    public void canParseFunctionCallWithNamedParameters() throws Exception {
        FunctionCall namedArguments = Grammar.FUNCTION_CALL.parse("template(foo=bar, baz=dan)").value();
        assertThat(namedArguments.name(), is(name("template")));
        NamedArguments arguments = (NamedArguments) namedArguments.arguments();
        assertThat(arguments.value().get("foo"), is(attribute(name("bar"))));
        assertThat(arguments.value().get("baz"), is(attribute(name("dan"))));
    }

    @Test
    public void canParseFunctionCallImplicitParameters() throws Exception {
        FunctionCall unnamed = Grammar.FUNCTION_CALL.parse("template(foo, bar, baz)").value();
        assertThat(unnamed.name(), is(name("template")));
        ImplicitArguments arguments = (ImplicitArguments) unnamed.arguments();
        List<Expression> value = arguments.value();
        assertThat(value.get(0), is(attribute(name("foo"))));
        assertThat(value.get(1), is(attribute(name("bar"))));
        assertThat(value.get(2), is(attribute(name("baz"))));
    }

    @Test
    public void canParseFunctionCallLiteralParameters() throws Exception {
        FunctionCall unnamed = Grammar.FUNCTION_CALL.parse("template(\"foo\")").value();
        assertThat(unnamed.name(), is(name("template")));
        ImplicitArguments arguments = (ImplicitArguments) unnamed.arguments();
        assertThat(((Text)arguments.value().get(0)).value().toString(), is("foo"));
    }

    @Test
    public void canParseImplicits() throws Exception {
        assertThat(Grammar.IMPLICIT_ARGUMENTS.parse("a").value().value().get(0), instanceOf(Attribute.class));
        assertThat(Grammar.IMPLICIT_ARGUMENTS.parse("a,b").value().value().get(1), instanceOf(Attribute.class));
        assertThat(Grammar.IMPLICIT_ARGUMENTS.parse("a,b").value().value().get(1), instanceOf(Attribute.class));
        assertThat(Grammar.IMPLICIT_ARGUMENTS.parse("\"a\"").value().value().get(0), instanceOf(Text.class));
    }

    @Test
    public void canParseValue() throws Exception {
        assertThat(Grammar.VALUE.parse("a").value(), instanceOf(Attribute.class));
        assertThat(Grammar.VALUE.parse("\"a\"").value(), instanceOf(Text.class));
    }

    @Test
    public void canParseLiteral() throws Exception {
        Text text = Grammar.LITERAL.parse("\"Some other text\"").value();
        assertThat(text.value().toString(), is("Some other text"));
    }

    @Test
    public void canParseAnonymousTemplate() throws Exception {
        Anonymous template = Grammar.ANONYMOUS_TEMPLATE.parse("{ name | Hello $name$ }").value();
        assertThat(template.paramaeterNames(), is(list("name")));
    }

    @Test
    public void supportsMapping() throws Exception {
        Mapping mapping = Grammar.MAPPING.parse("users:{ user | Hello $user$ }").value();
        assertThat(mapping.attribute(), is(attribute(name("users"))));
        assertThat(mapping.expression(), is(instanceOf(Anonymous.class)));
    }

    @Test
    public void supportsIndirectionInFunctionCall() throws Exception {
        FunctionCall unnamed = Grammar.FUNCTION_CALL.parse("(template)(foo, bar, baz)").value();
        Indirection indirection = (Indirection) unnamed.name();
        Attribute attribute = (Attribute) indirection.value();
        assertThat(attribute, is(attribute(name("template"))));
    }
}