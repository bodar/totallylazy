package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Xml;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Predicates.always;
import static com.googlecode.totallylazy.Unchecked.cast;

public class MutableTemplateGroup implements TemplateGroup {
    public static final String NO_NAME = "";
    protected final Map<String, MatchingRenderer> functions = new HashMap<>();
    private final TemplateGroup parent;

    public MutableTemplateGroup(TemplateGroup parent) {
        this.parent = parent;
    }

    public MutableTemplateGroup() {
        this(EmptyTemplateGroup.Instance);
    }

    public static TemplateGroup defaultEncoders() {
        return new MutableTemplateGroup().
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

    @Override
    public boolean contains(String name) {
        return functions.containsKey(normalise(name));
    }

    protected MatchingRenderer renderersFor(String name) {
        String normalisedName = normalise(name);
        if (!contains(normalisedName)) {
            create(normalisedName);
        }
        return functions.get(normalisedName);
    }

    protected void create(String normalisedName) {
        functions.put(normalisedName, new MatchingRenderer(parent.get(normalisedName)));
    }

    public static String normalise(String name) {
        return name.trim().toLowerCase();
    }

}
