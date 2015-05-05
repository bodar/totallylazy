package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.template.ast.Attribute;
import com.googlecode.totallylazy.template.ast.Template;
import com.googlecode.totallylazy.template.ast.TemplateCall;
import com.googlecode.totallylazy.template.ast.Text;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GrammarTest {
    @Test
    public void canParseAttribute() throws Exception {
        Attribute attribute = Grammar.ATTRIBUTE.parse("foo").value();
        assertThat(attribute.value(), is("foo"));
    }

    @Test
    public void canParseText() throws Exception {
        Text text = Grammar.TEXT.parse("Some other text").value();
        assertThat(text.value(), is("Some other text"));
    }

    @Test
    public void literalCanBeSingleOrDoubleQuoted() throws Exception {
        assertThat(Grammar.LITERAL.parse("\"foo\"").value().value(), is("foo"));
        assertThat(Grammar.LITERAL.parse("'foo'").value().value(), is("foo"));
    }

    @Test
    public void canParseNoArgumentTemplateCall() throws Exception {
        TemplateCall noArguments = Grammar.TEMPLATE_CALL.parse("template()").value();
        assertThat(noArguments.name(), is("template"));
    }

    @Test
    public void canParseAnExpression() throws Exception {
        assertThat(Grammar.EXPRESSION.parse("$template()$").value(), Matchers.instanceOf(TemplateCall.class));
        assertThat(Grammar.EXPRESSION.parse("$template$").value(), Matchers.instanceOf(Attribute.class));
    }

    @Test
    public void canParseTemplateCallWithNamedParameters() throws Exception {
        TemplateCall namedArguments = Grammar.TEMPLATE_CALL.parse("template(foo=bar, baz=dan)").value();
        assertThat(namedArguments.name(), is("template"));
        assertThat(((Attribute) namedArguments.arguments().get("foo")).value(), is("bar"));
        assertThat(((Attribute) namedArguments.arguments().get("baz")).value(), is("dan"));
    }

    @Test
    public void canParseTemplateCallImplicitParameters() throws Exception {
        TemplateCall unnamed = Grammar.TEMPLATE_CALL.parse("template(foo, bar, baz)").value();
        assertThat(unnamed.name(), is("template"));
        assertThat(((Attribute) unnamed.arguments().get("0")).value(), is("foo"));
        assertThat(((Attribute) unnamed.arguments().get("1")).value(), is("bar"));
        assertThat(((Attribute) unnamed.arguments().get("2")).value(), is("baz"));
    }

    @Test
    public void canParseTemplateCallLiteralParameters() throws Exception {
        TemplateCall unnamed = Grammar.TEMPLATE_CALL.parse("template(\"foo\")").value();
        assertThat(unnamed.name(), is("template"));
        assertThat(((Text) unnamed.arguments().get("0")).value(), is("foo"));
    }

    @Test
    public void canParseImplicits() throws Exception {
        assertThat(Grammar.IMPLICIT_ARGUMENTS.parse("a").value().get("0"), Matchers.instanceOf(Attribute.class));
        assertThat(Grammar.IMPLICIT_ARGUMENTS.parse("a,b").value().get("1"), Matchers.instanceOf(Attribute.class));
        assertThat(Grammar.IMPLICIT_ARGUMENTS.parse("a,b").value().get("1"), Matchers.instanceOf(Attribute.class));
        assertThat(Grammar.IMPLICIT_ARGUMENTS.parse("\"a\"").value().get("0"), Matchers.instanceOf(Text.class));
    }

    @Test
    public void canParseValue() throws Exception {
        assertThat(Grammar.VALUE.parse("a").value(), Matchers.instanceOf(Attribute.class));
        assertThat(Grammar.VALUE.parse("\"a\"").value(), Matchers.instanceOf(Text.class));
    }

    @Test
    public void canParseLiteral() throws Exception {
        Text text = Grammar.LITERAL.parse("\"Some other text\"").value();
        assertThat(text.value(), is("Some other text"));
    }


    @Test
    public void canParseATemplate() throws Exception {
        Funclate funclate = new CompositeFunclate();
        funclate.add("subTemplateA", ignore -> "...");
        funclate.add("subTemplateB", (Map<String, Object> context) -> "Your last name is " + context.get("name"));
        Template template = new Template(funclate, "Hello $first$ $subTemplateA()$ $subTemplateB(name=last)$");
        String result = template.render(map("first", "Dan", "last", "Bodart"));
        assertThat(result, is("Hello Dan ... Your last name is Bodart"));
    }
}
