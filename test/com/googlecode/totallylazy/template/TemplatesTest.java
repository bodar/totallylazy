package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Strings;
import org.junit.Test;

import java.io.PrintWriter;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Strings.startsWith;
import static com.googlecode.totallylazy.predicates.Predicates.contains;
import static com.googlecode.totallylazy.predicates.Predicates.instanceOf;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.template.Templates.templates;

public class TemplatesTest {
    @Test
    public void registeredRenderersAreCorrectlyCalled() throws Exception {
        Templates templates = templates(getClass()).extension("st");
        templates.add(instanceOf(String.class), s -> Strings.string(s.hashCode()));
        String value = "Dan";
        assertThat(templates.get("hello").render(map("name", value)), is("Hello " + value.hashCode()));
        assertThat(templates.get("parent").render(map("name", value)), is("Say Hello " + value.hashCode()));
    }


    @Test
    public void defaultTemplatesSupportsEncoding() throws Exception {
        Templates templates = templates().addDefault();
        Template template = Template.template("Hello $html(first)$", templates);
        String result = template.render(map("first", "<Dan>"));
        assertThat(result, is("Hello &lt;Dan&gt;"));
    }

    @Test
    public void canLoadFromClassPath() throws Exception {
        Templates templates = templates(getClass()).extension("st");
        String result = templates.get("hello").render(map("name", "Dan"));
        assertThat(result, is("Hello Dan"));
    }

    @Test
    public void supportsIndirection() throws Exception {
        Templates templates = templates().addDefault();
        Template template = Template.template("Hello $(encoding)(first)$", templates);
        String result = template.render(map("first", "<Dan>", "encoding", "html"));
        assertThat(result, is("Hello &lt;Dan&gt;"));
    }

    @Test
    public void canUseTemplatesFromClassPathAndDefaultTemplates() throws Exception {
        Templates templates = templates(getClass()).addDefault().extension("st");
        Template template = Template.template("Say $hello()$", templates);
        String result = template.render(map("name", "Dan"));
        assertThat(result, is("Say Hello Dan"));
    }

    @Test
    public void doesNotThrowWhenASubTemplateIsNotFoundButReturnsEmptyStringAndLogs() throws Exception {
        StringBuilder log = new StringBuilder();
        Templates templates = templates(getClass()).logger(log).extension("st");
        String result = templates.get("error").render(map());
        assertThat(result, is("Sub template returned ''"));
        assertThat(log.toString(), startsWith("Unable to load template 'foo.st' because: "));
    }
}