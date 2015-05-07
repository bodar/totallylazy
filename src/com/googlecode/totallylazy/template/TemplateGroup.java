package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Unchecked;

public interface TemplateGroup extends Renderers {
    TemplateGroup add(String name, Renderer<?> renderer);

    default TemplateGroup add(String name, Callable1<?, ? extends CharSequence> callable) {
        return add(name, (instance, appendable) ->
                appendable.append(Unchecked.<Callable1<Object, CharSequence>>cast(callable).call(instance)));
    }

    <T> TemplateGroup add(Predicate<? super T> predicate, Renderer<? super T> renderer);

    default <T> TemplateGroup add(Predicate<? super T> predicate, Callable1<? super T, ? extends CharSequence> renderer) {
        return add(predicate, new Renderer<T>() {
            @Override
            public Appendable render(T instance, Appendable appendable) throws Exception {
                return appendable.append(renderer.call(instance));
            }
        });
    }
}
