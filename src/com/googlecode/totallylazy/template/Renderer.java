package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Strings;

import static com.googlecode.totallylazy.Unchecked.cast;

public interface Renderer<T> {
    default String render(T instance) throws Exception {
        return render(instance, new StringBuilder()).toString();
    }

    <A extends Appendable> A render(final T instance, final A appendable) throws Exception;

}
