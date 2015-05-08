package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Xml;

import java.net.URLEncoder;
import java.util.concurrent.ConcurrentHashMap;

import static com.googlecode.totallylazy.Predicates.always;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.template.CompositeRenderer.compositeRenderer;

public class MutableTemplateGroup implements TemplateGroup {
    private final Renderers parent;
    private final ConcurrentHashMap<String, CompositeRenderer> named = new ConcurrentHashMap<>();
    private CompositeRenderer implicit;

    public MutableTemplateGroup(Renderers parent) {
        this.parent = parent;
        implicit = compositeRenderer(parent);
    }

    public MutableTemplateGroup() {
        this(Renderers.Empty.Instance);
    }

    public static MutableTemplateGroup defaultEncoders() {
        return defaultEncoders(new MutableTemplateGroup());
    }

    private static MutableTemplateGroup defaultEncoders(MutableTemplateGroup mutableTemplateGroup) {
        return mutableTemplateGroup.
                add("raw", Default.Instance).
                add("html", Xml.escape()).
                add("xml", Xml.escape()).
                add("url", urlEncode());
    }

    private static Callable1<String, String> urlEncode() {
        return s -> URLEncoder.encode(s, "UTF-8");
    }

    public <T> MutableTemplateGroup add(String name, Predicate<? super T> predicate, Renderer<? super T> callable) {
        named.compute(name, (n, old) -> (old == null ? create(n) : old).add(predicate, callable));
        return this;
    }

    @Override
    public MutableTemplateGroup add(String name, Callable1<?, ? extends CharSequence> callable) {
        TemplateGroup.super.add(name, callable);
        return this;
    }

    @Override
    public <T> MutableTemplateGroup add(Predicate<? super T> predicate, Callable1<? super T, ? extends CharSequence> renderer) {
        TemplateGroup.super.add(predicate, renderer);
        return this;
    }

    @Override
    public MutableTemplateGroup add(String name, Renderer<?> callable) {
        return add(name, always(), cast(callable));
    }

    @Override
    public <T> MutableTemplateGroup add(Predicate<? super T> predicate, Renderer<? super T> renderer) {
        implicit = implicit.add(predicate, renderer);
        return this;
    }

    @Override
    public Appendable render(Object instance, Appendable appendable) throws Exception {
        return implicit.render(instance, appendable);
    }

    @Override
    public Renderer<Object> get(String name) {
        return named.computeIfAbsent(name, this::create);
    }

    private CompositeRenderer create(String name) {return compositeRenderer(parent.get(name));}
}
