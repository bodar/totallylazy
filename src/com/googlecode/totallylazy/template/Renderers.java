package com.googlecode.totallylazy.template;

import java.io.IOException;

public abstract class Renderers extends Renderer<Object> {
    public abstract Renderer<Object> get(String name);

    public static class Empty extends Renderers {
        public static final Renderers Instance = new Empty();

        @Override
        public Renderer<Object> get(String name) {
            return Default.Instance;
        }

        @Override
        public Appendable render(Object instance, Appendable appendable) throws IOException {
            return Default.Instance.render(instance, appendable);
        }
    }
}
