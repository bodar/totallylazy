package com.googlecode.totallylazy.template;

public interface Renderer<T> {
    default String render(T instance) throws Exception {
        return render(instance, new StringBuffer()).toString();
    }

    <A extends Appendable> A render(final T instance, final A appendable) throws Exception;
}
