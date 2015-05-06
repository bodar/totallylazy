package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Strings;

public enum DefaultRenderer implements Renderer<Object> {
    Instance;

    @Override
    public Appendable render(Object instance, Appendable appendable) throws Exception {
        return appendable.append(Strings.asString(instance));
    }
}
