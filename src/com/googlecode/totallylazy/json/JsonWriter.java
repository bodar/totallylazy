package com.googlecode.totallylazy.json;

import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.annotations.multimethod;
import com.googlecode.totallylazy.multi;
import com.googlecode.totallylazy.time.Dates;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Appendables.append;

public interface JsonWriter {
    static final String SEPARATOR = ",";

    class State { private static multi multi; }

    static <A extends Appendable> A write(final Object o, final A appendable) {
        if(State.multi == null) State.multi = new multi(){};
        return State.multi.<A>methodOption(o, appendable).getOrElse(() -> write(o.toString(), appendable));
    }

    @multimethod
    static <A extends Appendable> A write(final CharSequence charSequence, final A appendable) {
        return append(Strings.toString(charSequence), appendable);
    }

    @multimethod
    static <A extends Appendable> A write(final Iterator<?> iterator, final A appendable) {
        return iterate(iterator, appendable, "[", SEPARATOR, "]");
    }

    @multimethod
    static <A extends Appendable> A write(final Iterable<?> iterable, final A appendable) {
        if(iterable instanceof Map) return write((Map) iterable, appendable);
        return write(iterable.iterator(), appendable);
    }

    @multimethod
    static <A extends Appendable> A write(final Map<?, ?> map, final A appendable) {
        return iterate(map.entrySet().iterator(), appendable, "{", SEPARATOR, "}");
    }

    @multimethod
    static <A extends Appendable> A write(final Map.Entry<?, ?> entry, final A appendable) {
        return write(entry.getValue(), append(':', append(Strings.toString(String.valueOf(entry.getKey())), appendable)));
    }

    @multimethod
    static <A extends Appendable> A write(final Void aVoid, final A appendable) {
        return append("null", appendable);
    }

    @multimethod
    static <A extends Appendable> A write(final Number number, final A appendable) {
        return append(number.toString(), appendable);
    }

    @multimethod
    static <A extends Appendable> A write(final Date date, final A appendable) {
        return append(Strings.quote(Dates.RFC3339withMilliseconds().format(date)), appendable);
    }

    @multimethod
    static <A extends Appendable> A write(final Boolean bool, final A appendable) {
        return append(bool.toString(), appendable);
    }

    static <A extends Appendable> A iterate(final Iterator<?> iterator, final A appendable, String start, String separator, String end) {
        return Iterators.appendTo(Iterators.map(iterator, o -> {
            write(o, appendable);
            return "";
        }), appendable, start, separator, end);
    }
}
