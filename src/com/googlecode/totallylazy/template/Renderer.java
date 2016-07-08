package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Strings;

import java.io.IOException;
import java.util.concurrent.Callable;

public abstract class Renderer<T> {
    public String render(T instance) throws IOException {
        return render(instance, new StringBuilder()).toString();
    }

    public abstract Appendable render(final T instance, final Appendable appendable) throws IOException;

    public static class Default extends Renderer<Object> {
        public static final Renderer<Object> Instance = new Default();

        @Override
        public Appendable render(Object instance, Appendable appendable) throws IOException {
            if(instance instanceof CharSequence) return appendable.append((CharSequence) instance);
            return appendable.append(Strings.asString(instance));
        }
    }

    static <T> Renderer<T> lazy(final Callable<Renderer<T>> callable){
        return new Renderer<T>() {
            @Override
            public Appendable render(T instance, Appendable appendable) throws IOException {
                try {
                    return callable.call().render(instance, appendable);
                } catch (Exception e) {
                    throw new IOException(e);
                }
            }
        };
    }
}
