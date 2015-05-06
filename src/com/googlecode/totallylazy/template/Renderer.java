package com.googlecode.totallylazy.template;

public interface Renderer<T> {
    default String render(T instance) throws Exception {
        return render(instance, new StringBuilder()).toString();
    }

    Appendable render(final T instance, final Appendable appendable) throws Exception;
}
