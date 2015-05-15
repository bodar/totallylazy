package com.googlecode.totallylazy.template;

import java.io.IOException;

public interface Renderers extends Renderer<Object> {
    Renderer<Object> get(String name);

    enum Empty implements Renderers {
        Instance;

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
