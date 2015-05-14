package com.googlecode.totallylazy.template;

public interface Renderers extends Renderer<Object> {
    Renderer<Object> get(String name);

    enum Empty implements Renderers {
        Instance;

        @Override
        public Renderer<Object> get(String name) {
            return Default.Instance;
        }

        @Override
        public Appendable render(Object instance, Appendable appendable) throws Exception {
            return Default.Instance.render(instance, appendable);
        }
    }
}
