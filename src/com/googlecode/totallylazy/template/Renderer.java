package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Strings;

import java.io.IOException;
import java.util.concurrent.Callable;

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

    static <T> Renderer<T> lazy(Callable<Renderer<T>> callable){
        return (instance, appendable) -> {
            try {
                return callable.call().render(instance, appendable);
            } catch (Exception e) {
                throw new IOException(e);
            }
        };
    }
}
