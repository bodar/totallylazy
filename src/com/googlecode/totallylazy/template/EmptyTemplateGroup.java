package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Strings;

import static com.googlecode.totallylazy.Unchecked.cast;

public enum EmptyTemplateGroup implements TemplateGroup {
    Instance;

    @Override
    public TemplateGroup add(String name, Renderer<?> renderer) {
        return this;
    }

    @Override
    public <T> TemplateGroup add(Predicate<? super T> predicate, Renderer<? super T> renderer) {
        return this;
    }

    @Override
    public Renderer<Object> get(String name) {
        return DefaultRenderer.Instance;
    }

    @Override
    public Appendable render(Object instance, Appendable appendable) throws Exception {
        return DefaultRenderer.Instance.render(instance, appendable);
    }
}
