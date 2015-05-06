package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Strings;

import static com.googlecode.totallylazy.Unchecked.cast;

public interface TemplateGroup extends Renderer<Object> {
    Renderer<Object> get(String name);

    boolean contains(String name);

    static TemplateGroup defaultTemplateGroup() {
        return new TemplateGroup() {
            @Override
            public Renderer<Object> get(String name) {
                return this;
            }

            @Override
            public boolean contains(String name) {
                return true;
            }

            @Override
            public <A extends Appendable> A render(Object instance, A appendable) throws Exception {
                return cast(appendable.append(Strings.asString(instance)));
            }
        };
    }


}
