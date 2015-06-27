package com.googlecode.totallylazy.template;

import org.junit.Test;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.predicates.Predicates.is;

public class UrlRenderersTest {
    @Test
    public void canLoadTemplatesRelativeToClass() throws Exception {
        Renderers renderers = UrlRenderers.renderers(getClass());
        String result = renderers.get("hello").render(map("name", "Dan"));
        assertThat(result, is("Hello Dan"));
    }

    @Test
    public void canCallSubTemplates() throws Exception {
        Renderers renderers = UrlRenderers.renderers(getClass());
        String result = renderers.get("parent").render(map("name", "Dan"));
        assertThat(result, is("Say Hello Dan"));
    }
}