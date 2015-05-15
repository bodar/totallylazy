package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Strings;

import java.io.IOException;

public interface Renderer<T> {
    default String render(T instance) throws IOException {
        return render(instance, new StringBuilder()).toString();
    }

    Appendable render(final T instance, final Appendable appendable) throws IOException;

    enum Default implements Renderer<Object> {
        Instance;

        @Override
        public Appendable render(Object instance, Appendable appendable) throws IOException {
            if(instance instanceof CharSequence) return appendable.append((CharSequence) instance);
            return appendable.append(Strings.asString(instance));
        }
    }
}
