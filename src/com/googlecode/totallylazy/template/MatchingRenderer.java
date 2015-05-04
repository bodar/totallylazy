package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;

public class MatchingRenderer implements Renderer<Object>{
    private final Deque<Pair<Predicate<Object>, Callable1<Object, String>>> pairs = new ArrayDeque<Pair<Predicate<Object>, Callable1<Object, String>>>();
    private Callable1<Object, String> noMatchRenderer;

    public MatchingRenderer() {
        noMatchRenderer = Callables.asString();
    }

    public MatchingRenderer(Renderer<Object> noMatchRenderer) {
        this.noMatchRenderer = callable(noMatchRenderer);
    }

    public MatchingRenderer parent(Renderer<Object> noMatchRenderer) {
        this.noMatchRenderer = callable(noMatchRenderer);
        return this;
    }

    public String render(Object value) throws Exception {
        Predicate<Predicate<Object>> matches = Predicates.matches(value);
        return sequence(pairs).find(where(Callables.<Predicate<Object>>first(), matches)).
                map(Callables.<Callable1<Object, String>>second()).
                getOrElse(noMatchRenderer).
                call(value);
    }

    public <T, R> MatchingRenderer add(Predicate<? super T> predicate, Renderer<? super T> renderer) {
        return add(predicate, callable(renderer));
    }

    @SuppressWarnings("unchecked")
    public <T, R> MatchingRenderer add(Predicate<? super T> predicate, Callable1<? super T, String> callable) {
        pairs.addFirst(Pair.<Predicate<Object>, Callable1<Object, String>>pair((Predicate<Object>) predicate, (Callable1<Object, String>) callable));
        return this;
    }

    public static <T> Callable1<T, String> callable(final Renderer<T> renderer) {
        return renderer::render;
    }

}
