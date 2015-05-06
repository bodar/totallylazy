package com.googlecode.totallylazy.template;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.template.Template.template;
import static org.hamcrest.MatcherAssert.assertThat;

public class TemplateTest {
    @Test
    public void supportsToString() throws Exception {
        String template = "Hello $name$ $template()$ $yourLastName(template())$";
        assertThat(template(template).toString(), is(template));
    }

    @Test
    public void canParseATemplate() throws Exception {
        Template template = template("Hello $first$ $last$");
        String result = template.render(map("first", "Dan", "last", "Bodart"));
        assertThat(result, Matchers.is("Hello Dan Bodart"));
    }

    @Test
    public void canCallSubTemplates() throws Exception {
        TemplateGroup templateGroup = new MutableTemplateGroup().
            add("subTemplateA", ignore -> "...").
            add("subTemplateB", (Map<String, Object> context) -> "Your last name is " + context.get("name"));
        Template template = template("Hello $first$ $subTemplateA()$ $subTemplateB(name=last)$", templateGroup);
        String result = template.render(map(
                "first", "Dan",
                "last", "Bodart"));
        assertThat(result, Matchers.is("Hello Dan ... Your last name is Bodart"));
    }

}
