package com.googlecode.totallylazy.template;

import org.junit.Test;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.callables.TimeReport.time;
import static com.googlecode.totallylazy.template.Templates.defaultTemplates;
import static com.googlecode.totallylazy.template.Templates.templates;

public class TemplatesTest {
    @Test
    public void defaultTemplatesSupportsEncoding() throws Exception {
        Templates templates = defaultTemplates();
        Template template = Template.template("Hello $html(first)$", templates);
        String result = template.render(map("first", "<Dan>"));
        assertThat(result, is("Hello &lt;Dan&gt;"));
    }

    @Test
    public void canLoadFromClassPath() throws Exception {
        Templates templates = templates(getClass());
        String result = templates.get("hello").render(map("name", "Dan"));
        assertThat(result, is("Hello Dan"));
    }
}