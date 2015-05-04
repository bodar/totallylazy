package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Strings;

public interface RendererContainer {
    Renderer<Object> get(String name);

    public static class methods {
        public static RendererContainer noParent() {
            return name -> Strings::asString;
        }
    }
}
