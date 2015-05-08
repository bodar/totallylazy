package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Xml;

import java.net.URLEncoder;
import java.util.concurrent.ConcurrentHashMap;

import static com.googlecode.totallylazy.Predicates.always;
import static com.googlecode.totallylazy.Unchecked.cast;

public class MutableTemplateGroup implements TemplateGroup {
    public static final String NO_NAME = "";
    private final ConcurrentHashMap<String, CompositeRenderer> functions = new ConcurrentHashMap<>();
    private final Renderers parent;

    public MutableTemplateGroup(Renderers parent) {
        this.parent = parent;
    }

    public MutableTemplateGroup() {
        this(Renderers.Empty.Instance);
    }

    public static MutableTemplateGroup defaultEncoders() {
        return defaultEncoders(new MutableTemplateGroup());
    }

    private static MutableTemplateGroup defaultEncoders(MutableTemplateGroup mutableTemplateGroup) {
        return mutableTemplateGroup.
                add("raw", Callables.asString()).
                add("html", Xml.escape()).
                add("xml", Xml.escape()).
                add("url", urlEncode());
    }

    private static Callable1<String, String> urlEncode() {
        return s -> URLEncoder.encode(s, "UTF-8");
    }

    public <T> MutableTemplateGroup add(String name, Predicate<? super T> predicate, Renderer<? super T> callable) {
        renderersFor(name).add(predicate, callable);
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
        return add(NO_NAME, predicate, renderer);
    }

    @Override
    public Appendable render(Object instance, Appendable appendable) throws Exception {
        return get(NO_NAME).render(instance, appendable);
    }

    @Override
    public Renderer<Object> get(String name) {
        return renderersFor(name);
    }

    protected CompositeRenderer renderersFor(String name) {
        return functions.computeIfAbsent(normalise(name),
                (n) -> new CompositeRenderer(parent.get(n)));
    }

    public static String normalise(String name) {
        return name.trim().toLowerCase();
    }

}
