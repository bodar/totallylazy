package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Strings;

public interface Renderer<T> {
    default String render(T instance) throws Exception {
        return render(instance, new StringBuilder()).toString();
    }

    Appendable render(final T instance, final Appendable appendable) throws Exception;

    enum Default implements Renderer<Object> {
        Instance;

        @Override
        public Appendable render(Object instance, Appendable appendable) throws Exception {
            if(instance instanceof CharSequence) return appendable.append((CharSequence) instance);
            return appendable.append(Strings.asString(instance));
        }
    }
}
