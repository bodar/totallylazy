package com.googlecode.totallylazy.template;

import org.junit.Test;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Predicates.is;

public class TemplateGroupTest {
    @Test
    public void supportsEncoding() throws Exception {
        TemplateGroup templateGroup = MutableTemplateGroup.defaultEncoders();
        Template template = Template.template("Hello $html(first)$", templateGroup);
        String result = template.render(map("first", "<Dan>"));
        assertThat(result, is("Hello &lt;Dan&gt;"));
    }
}