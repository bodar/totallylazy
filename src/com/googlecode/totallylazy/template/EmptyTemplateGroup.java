package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Strings;

import static com.googlecode.totallylazy.Unchecked.cast;

/**
* Created by dan on 06/05/15.
*/
public class EmptyTemplateGroup implements TemplateGroup {
    @Override
    public <T> MutableTemplateGroup add(String name, Callable1<? super T, String> callable) {
        return null;
    }

    @Override
    public <T> MutableTemplateGroup add(Predicate<? super T> predicate, Callable1<? super T, String> renderer) {
        return null;
    }

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
}
