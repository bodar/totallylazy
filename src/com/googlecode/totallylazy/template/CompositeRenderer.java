package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.collections.PersistentList;

import java.io.IOException;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.always;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.list;

public class CompositeRenderer extends Renderer<Object> {
    private final PersistentList<Pair<Predicate<Object>, Renderer<Object>>> pairs;

    private CompositeRenderer(PersistentList<Pair<Predicate<Object>, Renderer<Object>>> pairs) {
        this.pairs = pairs;
    }

    public static CompositeRenderer compositeRenderer() {
        return compositeRenderer(Default.Instance);
    }

    public static CompositeRenderer compositeRenderer(Renderer<Object> defaultRenderer) {
        return new CompositeRenderer(list(pair((Predicate<Object>) always(), defaultRenderer)));
    }

    @Override
    public Appendable render(final Object value, Appendable appendable) throws IOException {
        return pairs.toSequence().
                find(new Predicate<Pair<Predicate<Object>, Renderer<Object>>>() {
                    @Override
                    public boolean matches(Pair<Predicate<Object>, Renderer<Object>> p) {
                        return p.first().matches(value);
                    }
                }).
                get().second().render(value, appendable);
    }

    public <T> CompositeRenderer add(Predicate<? super T> predicate, Renderer<? super T> renderer) {
        return new CompositeRenderer(pairs.cons(pair(Unchecked.<Predicate<Object>>cast(predicate), Unchecked.<Renderer<Object>>cast(renderer))));
    }
}
