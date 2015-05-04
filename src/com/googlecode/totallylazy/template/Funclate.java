package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Xml;

import java.net.URLEncoder;

import static com.googlecode.totallylazy.Predicates.always;

public interface Funclate extends RendererContainer, Renderer<Object> {
    <T> Funclate add(String name, Predicate<? super T> predicate, Callable1<? super T, String> callable);

    <T> Funclate add(String name, Callable1<? super T, String> callable);

    <T> Funclate add(Predicate<? super T> predicate, Callable1<? super T, String> callable);

    public static class methods{
        public static Funclate defaultFunclates() {
            return addDefaultEncoders(new CompositeFunclate());
        }

        public static Funclate addDefaultEncoders(Funclate funclate) {
            return funclate.
                    add("raw", always(), Callables.asString()).
                    add("html", always(), Xml.escape()).
                    add("xml", always(), Xml.escape()).
                    add("url", always(), urlEncode());
        }

        private static Callable1<String, String> urlEncode() {
            return s -> URLEncoder.encode(s, "UTF-8");
        }

    }
}
