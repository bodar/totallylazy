package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.collections.PersistentList;

import java.io.IOException;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.always;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.list;

public class CompositeRenderer implements Renderer<Object>{
    private final PersistentList<Pair<Predicate<Object>, Renderer<Object>>> pairs;

    private CompositeRenderer(PersistentList<Pair<Predicate<Object>, Renderer<Object>>> pairs) {
        this.pairs = pairs;
    }

    public static CompositeRenderer compositeRenderer() {
        return compositeRenderer(Default.Instance);
    }

    public static CompositeRenderer compositeRenderer(Renderer<Object> defaultRenderer) {
        return new CompositeRenderer(list(pair(always(), defaultRenderer)));
    }

    @Override
    public Appendable render(Object value, Appendable appendable) throws IOException {
        return pairs.find(p -> p.first().matches(value)).
                get().second().render(value, appendable);
    }

    public <T> CompositeRenderer add(Predicate<? super T> predicate, Renderer<? super T> renderer) {
        return new CompositeRenderer(pairs.cons(pair(Unchecked.<Predicate<Object>>cast(predicate), cast(renderer))));
    }
}
