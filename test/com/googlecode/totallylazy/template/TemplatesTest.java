package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Strings;
import org.junit.Test;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.template.Templates.templates;
import static org.hamcrest.MatcherAssert.assertThat;

public class TemplatesTest {
    @Test
    public void registeredRenderersAreCorrectlyCalled() throws Exception {
        Templates templates = templates(getClass()).extension("st");
        templates.add(Predicates.instanceOf(String.class), new Function1<Object, CharSequence>() {
            @Override
            public CharSequence call(Object o) throws Exception {
                return Strings.string(o.hashCode());
            }
        });
        String value = "Dan";
        assertThat(templates.get("hello").render(map("name", value)), is("Hello " + value.hashCode()));
        assertThat(templates.get("parent").render(map("name", value)), is("Say Hello " + value.hashCode()));
    }

    @Test
    public void defaultTemplatesSupportsEncoding() throws Exception {
        Templates templates = templates().addDefault();
        Template template = Template.template("Hello $html(first)$", templates);
        String result = template.render(map("first", (Object) "<Dan>"));
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
        String result = template.render(map("first", (Object) "<Dan>", "encoding", "html"));
        assertThat(result, is("Hello &lt;Dan&gt;"));
    }

    @Test
    public void canUseTemplatesFromClassPathAndDefaultTemplates() throws Exception {
        Templates templates = templates(getClass()).addDefault().extension("st");
        Template template = Template.template("Say $hello()$", templates);
        String result = template.render(map("name", (Object) "Dan"));
        assertThat(result, is("Say Hello Dan"));
    }
}