package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;

public class CompositeRenderer implements Renderer<Object>{
    private final Deque<Pair<Predicate<Object>, Renderer<Object>>> pairs = new ArrayDeque<>();
    private final Renderer<Object> defaultRenderer;

    public CompositeRenderer() {
        this(Default.Instance);
    }

    public CompositeRenderer(Renderer<Object> defaultRenderer) {
        this.defaultRenderer = defaultRenderer;
    }

    @Override
    public Appendable render(Object value, Appendable appendable) throws Exception {
        return sequence(pairs).find(p -> p.first().matches(value)).
                map(Pair::second).
                getOrElse(defaultRenderer).
                render(value, appendable);
    }

    public <T> CompositeRenderer add(Predicate<? super T> predicate, Renderer<? super T> renderer) {
        pairs.addFirst(pair(cast(predicate), cast(renderer)));
        return this;
    }
}
