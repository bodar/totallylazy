package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Uri;
import com.googlecode.totallylazy.Xml;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.googlecode.totallylazy.Strings.string;
import static com.googlecode.totallylazy.Unchecked.cast;

public class Templates extends Renderers {
    private final Function2<? super String, ? super Renderers, ? extends Renderer<?>> missing;
    private final ConcurrentMap<String, Renderer<Object>> renderers = new ConcurrentHashMap<>();
    private volatile CompositeRenderer implicits = CompositeRenderer.compositeRenderer();

    private Templates(Function2<? super String, ? super Renderers, ? extends Renderer<?>> missing) {
        this.missing = missing;
    }

    public static Templates templates() {
        return templates(Renderers.Empty.Instance);
    }

    public static Templates templates(Class<?> aClass) {
        return templates(UrlRenderers.renderers(aClass));
    }

    public static Templates templates(Uri uri) {
        return templates(UrlRenderers.renderers(uri));
    }

    public static Templates templates(final Renderers parent) {
        return templates(new Function2<String, Renderers, Renderer<?>>() {
            @Override
            public Renderer call(String name, Renderers renderers) throws Exception {
                return parent.get(name);
            }
        });
    }

    public static Templates templates(Function2<? super String, ? super Renderers, ? extends Renderer<?>> missing) {
        return new Templates(missing);
    }

    public Templates addDefault() {
        return add("raw", Default.Instance).
                add("html", Xml.escape()).
                add("xml", Xml.escape()).
                add("url", new Function1<Object, CharSequence>() {
                    @Override
                    public CharSequence call(Object o) throws Exception {
                        return URLEncoder.encode(string(o), "UTF-8");
                    }
                });
    }

    public <T> Templates add(final Predicate<? super T> predicate, final Function1<? super T, ? extends CharSequence> renderer) {
        return add(predicate, new Renderer<T>() {
            @Override
            public Appendable render(T instance, Appendable appendable) throws IOException {
                return appendable.append(renderer.apply(instance));
            }
        });
    }

    public <T> Templates add(Predicate<? super T> predicate, Renderer<? super T> renderer) {
        implicits = implicits.add(predicate, renderer);
        return this;
    }

    public Templates add(String name, final Function1<? super Object, ? extends CharSequence> renderer) {
        return add(name, new Renderer<Object>() {
            @Override
            public Appendable render(Object instance, Appendable appendable) throws IOException {
                return appendable.append(renderer.apply(instance));
            }
        });
    }

    public Templates add(String name, Renderer<Object> renderer) {
        renderers.put(name, renderer);
        return this;
    }

    @Override
    public Appendable render(Object instance, Appendable appendable) throws IOException {
        return implicits.render(instance, appendable);
    }

    @Override
    public Renderer<Object> get(final String name) {
        if (!renderers.containsKey(name)) {
            Renderer<Object> renderer = cast(missing.apply(name, Templates.this));
            renderers.putIfAbsent(name, renderer);
        }
        return renderers.get(name);
    }

    public Templates extension(final String value) {
        return new Templates(new Function2<String, Renderers, Renderer<?>>() {
            @Override
            public Renderer<?> call(String s, Renderers renderers) throws Exception {
                return missing.apply(s + "." + value, renderers);
            }
        });
    }
}
