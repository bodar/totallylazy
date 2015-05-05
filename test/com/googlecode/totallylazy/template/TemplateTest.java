package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.template.ast.Template;
import org.junit.Test;

import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TemplateTest {
    @Test
    public void supportsToString() throws Exception {
        String template = "Hello $name$ $template()$ $yourLastName(template())$";
        String toString = new Template(null, template).toString();
        assertThat(toString, is(template));

    }
}
