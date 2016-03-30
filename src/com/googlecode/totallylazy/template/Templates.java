package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.functions.Function2;
import com.googlecode.totallylazy.io.Uri;
import com.googlecode.totallylazy.predicates.Predicate;
import com.googlecode.totallylazy.predicates.Predicates;
import com.googlecode.totallylazy.xml.Xml;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.googlecode.totallylazy.Strings.string;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.predicates.Predicates.always;
import static com.googlecode.totallylazy.template.CompositeRenderer.compositeRenderer;

public class Templates implements Renderers {
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

    public static Templates templates(Renderers parent) {
        return templates((name, Renderers) -> parent.get(name));
    }

    public static Templates templates(Function2<? super String, ? super Renderers, ? extends Renderer<?>> missing) {
        return new Templates(missing);
    }

    public Templates addDefault() {
        return add("raw", Default.Instance).
                add("html", Xml.escape()).
                add("xml", Xml.escape()).
                add("url", s -> URLEncoder.encode(string(s), "UTF-8"));
    }

    public <T> Templates add(Predicate<? super T> predicate, Function1<? super T, ? extends CharSequence> renderer) {
        return add(predicate, (Renderer<T>) (o, a) -> a.append(renderer.apply(o)));
    }

    public <T> Templates add(Predicate<? super T> predicate, Renderer<? super T> renderer) {
        implicits = implicits.add(predicate, renderer);
        return this;
    }

    public Templates add(String name, Function1<Object, ? extends CharSequence> renderer) {
        return add(name, (o, a) -> a.append(renderer.apply(o)));
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
    public Renderer<Object> get(String name) {
        return renderers.computeIfAbsent(name, n -> cast(missing.apply(n, this)));
    }
}
