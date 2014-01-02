package com.googlecode.totallylazy;

import java.io.IOException;

public class MapSources implements Sources {
    private final Sources sources;
    private final Unary<Source> function;

    public MapSources(Sources sources, Unary<Source> function) {
        this.sources = sources;
        this.function = function;
    }

    static Sources mapSource(final Unary<Source> function, final Sources sources) {
        return new MapSources(sources, function);
    }

    @Override
    public Sequence<Source> sources() {
        return sources.sources().map(function);
    }

    @Override
    public void close() throws IOException {
        sources.close();
    }
}
