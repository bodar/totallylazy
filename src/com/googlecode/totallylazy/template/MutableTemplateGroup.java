package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Xml;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Predicates.always;
import static com.googlecode.totallylazy.template.TemplateGroup.defaultTemplateGroup;

public class MutableTemplateGroup implements TemplateGroup {
    public static final String NO_NAME = "";
    protected final Map<String, MatchingRenderer> funclates = new HashMap<>();
    private final TemplateGroup parent;

    public MutableTemplateGroup(TemplateGroup parent) {
        this.parent = parent;
    }

    public MutableTemplateGroup() {
        this(defaultTemplateGroup());
    }

    public static MutableTemplateGroup defaultEncoders() {
        return new MutableTemplateGroup().
                add("raw", always(), Callables.asString()).
                add("html", always(), Xml.escape()).
                add("xml", always(), Xml.escape()).
                add("url", always(), urlEncode());
    }

    private static Callable1<String, String> urlEncode() {
        return s -> URLEncoder.encode(s, "UTF-8");
    }


    public <T> MutableTemplateGroup add(String name, Predicate<? super T> predicate, Callable1<? super T, String> callable) {
        renderersFor(name).add(predicate, callable);
        return this;
    }

    public <T> MutableTemplateGroup add(String name, Callable1<? super T, String> callable) {
        return add(name, always(), callable);
    }

    public <T> MutableTemplateGroup add(Predicate<? super T> predicate, Callable1<? super T, String> renderer) {
        return add(NO_NAME, predicate, renderer);
    }

    @Override
    public <A extends Appendable> A render(Object instance, A appendable) throws Exception {
        return get(NO_NAME).render(instance, appendable);
    }

    @Override
    public Renderer<Object> get(String name) {
        return renderersFor(name);
    }

    @Override
    public boolean contains(String name) {
        return funclates.containsKey(normalise(name));
    }

    protected MatchingRenderer renderersFor(String name) {
        String normalisedName = normalise(name);
        if (!contains(normalisedName)) {
            create(normalisedName);
        }
        return funclates.get(normalisedName);
    }

    protected void create(String normalisedName) {
        funclates.put(normalisedName, new MatchingRenderer(parent.get(normalisedName)));
    }

    public static String normalise(String name) {
        return name.trim().toLowerCase();
    }

}
