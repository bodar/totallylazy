package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;

import static com.googlecode.totallylazy.Unchecked.cast;

public class UnfoldRightIterator<A, B> extends StatefulIterator<A> {
    private final Function<B, Option<Pair<A, B>>> callable;
    private B state;

    public UnfoldRightIterator(final Function<? super B, ? extends Option<? extends Pair<? extends A, ? extends B>>> callable, final B state) {
        this.callable = cast(callable);
        this.state = state;
    }

    @Override
    protected A getNext() throws Exception {
        Option<Pair<A, B>> result = callable.call(state);
        if (result.isEmpty()) return finished();
        Pair<A, B> pair = result.get();
        state = pair.second();
        return pair.first();
    }
}
