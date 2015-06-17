package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.Uri;
import com.googlecode.totallylazy.Xml;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.ConcurrentHashMap;

import static com.googlecode.totallylazy.Predicates.always;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.template.CompositeRenderer.compositeRenderer;

public class Templates implements Renderers {
    private final Renderers parent;
    private final ConcurrentHashMap<String, CompositeRenderer> named = new ConcurrentHashMap<>();
    private CompositeRenderer implicit;

    private Templates(Renderers parent) {
        this.parent = parent;
        implicit = compositeRenderer(parent);
    }

    public static Templates templates() {return templates(Renderers.Empty.Instance);}
    public static Templates templates(Class<?> aClass) {return templates(UrlRenderers.renderers(aClass));}
    public static Templates templates(Uri uri) {return templates(UrlRenderers.renderers(uri));}
    public static Templates templates(Renderers parent) {return new Templates(parent);}
    public static Templates defaultTemplates() { return defaultTemplates(Renderers.Empty.Instance);}
    public static Templates defaultTemplates(Class<?> aClass) {return defaultTemplates(UrlRenderers.renderers(aClass));}
    public static Templates defaultTemplates(Uri uri) {return defaultTemplates(UrlRenderers.renderers(uri));}
    public static Templates defaultTemplates(Renderers parent) {
        return templates(parent).
                add("raw", Default.Instance).
                add("html", Xml.escape()).
                add("xml", Xml.escape()).
                add("url", (String s) -> URLEncoder.encode(s, "UTF-8"));
    }


    public <T> Templates add(String name, Predicate<? super T> predicate, Renderer<? super T> callable) {
        named.compute(name, (n, old) -> (old == null ? create(n) : old).add(predicate, callable));
        return this;
    }

    public Templates add(String name, Callable1<?, ? extends CharSequence> callable) {
        return add(name, (instance, appendable) -> {
            try {
                return appendable.append(Unchecked.<Callable1<Object, CharSequence>>cast(callable).call(instance));
            } catch (Exception e) {
                throw new IOException(e);
            }
        });
    }

    public <T> Templates add(Predicate<? super T> predicate, Callable1<? super T, ? extends CharSequence> renderer) {
        return add(predicate, (instance, appendable) -> {
            try {
                return appendable.append(renderer.call(instance));
            } catch (Exception e) {
                throw new IOException(e);
            }
        });
    }

    public Templates add(String name, Renderer<?> callable) {
        return add(name, always(), cast(callable));
    }

    public <T> Templates add(Predicate<? super T> predicate, Renderer<? super T> renderer) {
        implicit = implicit.add(predicate, renderer);
        return this;
    }

    @Override
    public Appendable render(Object instance, Appendable appendable) throws IOException {
        return implicit.render(instance, appendable);
    }

    @Override
    public Renderer<Object> get(String name) {
        return named.computeIfAbsent(name, this::create);
    }

    private CompositeRenderer create(String name) {
        return compositeRenderer(Renderer.lazy( () -> parent.get(name)));
    }
}
