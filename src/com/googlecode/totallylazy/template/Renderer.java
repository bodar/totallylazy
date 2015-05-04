package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Mapper;

import static com.googlecode.totallylazy.Callables.flip;
import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Mapper.mapper;

public interface Renderer<T> {
    String render(T instance) throws Exception;

    class constructors {
        public static <T> Renderer<T> renderer(final Callable1<T, String> callable) {
            return callable::call;
        }
    }

    class functions {
        public static <T> Mapper<Renderer<T>, String> render(final T instance) {
            return mapper(call(functions.<T>render(), instance));
        }

        public static <T> Mapper<T, String> render(final Renderer<T> renderer) {
            return mapper(call(flip(functions.<T>render()), renderer));
        }

        public static <T> Function2<T, Renderer<T>, String> render() {
            return new Function2<T, Renderer<T>, String>() {
                @Override
                public String call(T t, Renderer<T> renderer) throws Exception {
                    return renderer.render(t);
                }
            };
        }

    }
}
