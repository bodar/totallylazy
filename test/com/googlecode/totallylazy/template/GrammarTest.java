package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.template.ast.Anonymous;
import com.googlecode.totallylazy.template.ast.Arguments;
import com.googlecode.totallylazy.template.ast.Attribute;
import com.googlecode.totallylazy.template.ast.Expression;
import com.googlecode.totallylazy.template.ast.Grammar;
import com.googlecode.totallylazy.template.ast.FunctionCall;
import com.googlecode.totallylazy.template.ast.ImplicitArguments;
import com.googlecode.totallylazy.template.ast.Indirection;
import com.googlecode.totallylazy.template.ast.Mapping;
import com.googlecode.totallylazy.template.ast.NamedArguments;
import com.googlecode.totallylazy.template.ast.Text;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Lists.list;
import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.Predicates.is;

public class GrammarTest {
    @Test
    public void canParseSingleAttribute() throws Exception {
        Attribute attribute = Grammar.ATTRIBUTE.parse("foo").value();
        assertThat(attribute.value(), is(list("foo")));
    }

    @Test
    public void canParseAttributeWithSubAttribute() throws Exception {
        Attribute attribute = Grammar.ATTRIBUTE.parse("foo.bar").value();
        assertThat(attribute.value(), is(list("foo", "bar")));
    }

    @Test
    public void canParseText() throws Exception {
        Text text = Grammar.TEXT.parse("Some other text").value();
        assertThat(text.charSequence().toString(), is("Some other text"));
    }

    @Test
    public void literalCanBeSingleOrDoubleQuoted() throws Exception {
        assertThat(Grammar.LITERAL.parse("\"foo\"").value().charSequence().toString(), is("foo"));
        assertThat(Grammar.LITERAL.parse("'foo'").value().charSequence().toString(), is("foo"));
    }

    @Test
    public void canParseAnExpression() throws Exception {
        assertThat(Grammar.EXPRESSION.parse("$template()$").value(), instanceOf(FunctionCall.class));
        assertThat(Grammar.EXPRESSION.parse("$template$").value(), instanceOf(Attribute.class));
    }

    @Test
    public void canParseFunctionCallWithNoParameters() throws Exception {
        FunctionCall unnamed = Grammar.FUNCTION_CALL.parse("template()").value();
        assertThat(unnamed.name(), is("template"));
        ImplicitArguments arguments = (ImplicitArguments) unnamed.arguments();
        assertThat(arguments.value().isEmpty(), is(true));
    }

    @Test
    public void canParseFunctionCallWithNamedParameters() throws Exception {
        FunctionCall namedArguments = Grammar.FUNCTION_CALL.parse("template(foo=bar, baz=dan)").value();
        assertThat(namedArguments.name(), is("template"));
        NamedArguments arguments = (NamedArguments) namedArguments.arguments();
        assertThat(((Attribute) arguments.value().get("foo")).value(), is(list("bar")));
        assertThat(((Attribute) arguments.value().get("baz")).value(), is(list("dan")));
    }

    @Test
    public void canParseFunctionCallImplicitParameters() throws Exception {
        FunctionCall unnamed = Grammar.FUNCTION_CALL.parse("template(foo, bar, baz)").value();
        assertThat(unnamed.name(), is("template"));
        ImplicitArguments arguments = (ImplicitArguments) unnamed.arguments();
        List<Expression> value = arguments.value();
        assertThat(((Attribute)value.get(0)).value(), is(list("foo")));
        assertThat(((Attribute)value.get(1)).value(), is(list("bar")));
        assertThat(((Attribute)value.get(2)).value(), is(list("baz")));
    }


    @Test
    public void canParseFunctionCallLiteralParameters() throws Exception {
        FunctionCall unnamed = Grammar.FUNCTION_CALL.parse("template(\"foo\")").value();
        assertThat(unnamed.name(), is("template"));
        ImplicitArguments arguments = (ImplicitArguments) unnamed.arguments();
        assertThat(((Text)arguments.value().get(0)).charSequence().toString(), is("foo"));
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
        assertThat(text.charSequence().toString(), is("Some other text"));
    }

    @Test
    public void canParseAnonymousTemplate() throws Exception {
        Anonymous template = Grammar.ANONYMOUS_TEMPLATE.parse("{ name | Hello $name$ }").value();
        assertThat(template.paramaeterNames(), is(list("name")));
    }

    @Test
    public void supportsMapping() throws Exception {
        Mapping mapping = Grammar.MAPPING.parse("users:{ user | Hello $user$ }").value();
        assertThat(mapping.attribute().value(), is(list("users")));
        assertThat(mapping.expression(), is(instanceOf(Anonymous.class)));
    }

    @Test
    public void supportsIndirectionInFunctionCall() throws Exception {
        FunctionCall unnamed = Grammar.FUNCTION_CALL.parse("(template)(foo, bar, baz)").value();
        Indirection indirection = (Indirection) unnamed.name();
        Attribute attribute = (Attribute) indirection.expression();
        assertThat(attribute.value(), is(list("template")));
    }
}