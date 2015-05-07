package com.googlecode.totallylazy.template;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Map;

import static com.googlecode.totallylazy.Lists.list;
import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.template.Template.template;
import static org.hamcrest.MatcherAssert.assertThat;

public class TemplateTest {
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

    @Test
    public void supportsMappingList() throws Exception {
        Template template = template("$users:{ user | Hello $user$ }$");
        String result = template.render(map("users", list("Dan", "Bob")));
        assertThat(result, is("Hello Dan Hello Bob "));
    }

    @Test
    public void supportsMappingSingleValues() throws Exception {
        Template template = template("$users:{ user | Hello $user$ }$");
        String result = template.render(map("users", "Dan"));
        assertThat(result, is("Hello Dan "));
    }

    @Test
    public void supportsMappingListWithIndex() throws Exception {
        Template template = template("$users:{ user, index | Hello $user$ you are number $index$!\n}$");
        String result = template.render(map("users", list("Dan", "Bob")));
        assertThat(result, is("Hello Dan you are number 0!\n" +
                "Hello Bob you are number 1!\n"));
    }

    @Test
    public void supportsMappingMaps() throws Exception {
        Template template = template("$user:{ value, key | $key$ $value$, }$");
        String result = template.render(map("user", map("name","Dan", "age", 12)));
        assertThat(result, is("name Dan, age 12, "));
    }

    @Test
    public void supportsMappingMapValues() throws Exception {
        Template template = template("$user:{ value | $value$ }$");
        String result = template.render(map("user", map("name","Dan", "age", 12)));
        assertThat(result, is("Dan 12 "));
    }


}
