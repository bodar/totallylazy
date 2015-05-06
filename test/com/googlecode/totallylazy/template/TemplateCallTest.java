package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.template.ast.Grammar;
import org.junit.Test;

import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TemplateCallTest {
    @Test
    public void supportsToString() throws Exception {
        String template = "template(foo=bar, baz=\"dan\")";
        String toString = Grammar.TEMPLATE_CALL.parse(template).value().toString();
        assertThat(toString, is(template));

    }
}
