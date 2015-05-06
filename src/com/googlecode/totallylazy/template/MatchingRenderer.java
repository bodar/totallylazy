package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Unchecked;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;

public class MatchingRenderer implements Renderer<Object>{
    private final Deque<Pair<Predicate<Object>, Renderer<Object>>> pairs = new ArrayDeque<>();
    private final Renderer<Object> defaultRenderer;

    public MatchingRenderer() {
        this(DefaultRenderer.Instance);
    }

    public MatchingRenderer(Renderer<Object> defaultRenderer) {
        this.defaultRenderer = defaultRenderer;
    }

    @Override
    public Appendable render(Object value, Appendable appendable) throws Exception {
        Predicate<Predicate<Object>> matches = Predicates.matches(value);
        return sequence(pairs).find(where(Callables.<Predicate<Object>>first(), matches)).
                map(Callables.<Renderer<Object>>second()).
                getOrElse(defaultRenderer).
                render(value, appendable);
    }

    public <T, R> MatchingRenderer add(Predicate<? super T> predicate, Renderer<? super T> renderer) {
        pairs.addFirst(pair(
                Unchecked.<Predicate<Object>>cast(predicate),
                Unchecked.<Renderer<Object>>cast(renderer)));
        return this;
    }

    public static <T> Callable1<T, String> callable(final Renderer<T> renderer) {
        return renderer::render;
    }

}
