package com.googlecode.totallylazy.io;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.predicates.Predicate;

import java.io.IOException;

public class FilterSource implements Sources {
    private final Predicate<? super Source> predicate;
    private final Sources sources;

    private FilterSource(Predicate<? super Source> predicate, Sources sources) {
        this.predicate = predicate;
        this.sources = sources;
    }

    public static FilterSource filterSource(Predicate<? super Source> predicate, Sources sources) {
        return new FilterSource(predicate, sources);
    }

    @Override
    public Sequence<Source> sources() {
        return sources.sources().filter(predicate);
    }

    @Override
    public void close() throws IOException {
        sources.close();
    }
}
