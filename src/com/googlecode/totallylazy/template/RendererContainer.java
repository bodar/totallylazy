package com.googlecode.totallylazy.template;

public interface RendererContainer {
    Renderer<Object> get(String name);

    boolean contains(String name);

    public static class methods {
        public static RendererContainer noParent() {
            return new RendererContainer() {
                @Override
                public Renderer<Object> get(String name) {
                    return new Renderer<Object>() {
                        @Override
                        public <A extends Appendable> A render(Object instance, A appendable) throws Exception {
                            appendable.append(instance.toString());
                            return appendable;
                        }
                    };
                }

                @Override
                public boolean contains(String name) {
                    return true;
                }
            };
        }
    }
}
