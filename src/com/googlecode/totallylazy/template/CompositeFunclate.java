package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Triple;

import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Predicates.always;
import static com.googlecode.totallylazy.template.RendererContainer.methods.noParent;

public class CompositeFunclate implements Funclate {
    public static final String NO_NAME = "";
    protected final Map<String, MatchingRenderer> funclates = new HashMap<>();
    private final RendererContainer parent;

    public CompositeFunclate(RendererContainer parent) {
        this.parent = parent;
    }

    public CompositeFunclate() {
        this(noParent());
    }

    public <T> Funclate add(String name, Predicate<? super T> predicate, Callable1<? super T, String> callable) {
        renderersFor(name).add(predicate, callable);
        return this;
    }

    public <T> Funclate add(String name, Callable1<? super T, String> callable) {
        return add(name, always(), callable);
    }

    public <T> Funclate add(Predicate<? super T> predicate, Callable1<? super T, String> renderer) {
        return add(NO_NAME, predicate, renderer);
    }

    @Override
    public <A extends Appendable> A render(Object instance, A appendable) throws Exception {
        return get(NO_NAME).render(instance, appendable);
    }

    public Renderer<Object> get(String name) {
        return renderersFor(name);
    }

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
