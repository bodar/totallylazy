package com.googlecode.totallylazy.time;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.text.DateFormat;
import java.util.Date;

import static com.googlecode.totallylazy.Callables.callThrows;
import static com.googlecode.totallylazy.Exceptions.ignoringException;

public class DateFormatConverter implements DateConverter {
    private final Sequence<DateFormat> formats;

    public DateFormatConverter(DateFormat... formats) {
        this(Sequences.sequence(formats));
    }

    public DateFormatConverter(Iterable<DateFormat> formats) {
        this.formats = Sequences.sequence(formats);
        if(this.formats.isEmpty()) {
            throw new IllegalArgumentException("No format specified");
        }
    }

    public static DateFormatConverter defaultConverter() {
        return new DateFormatConverter(Dates.RFC3339(), Dates.RFC822(), Dates.javaUtilDateToString());
    }

    public String toString(final Date value) {
        return formats.head().format(value);
    }

    public Date toDate(final String value) {
        Callable1<DateFormat, Option<Date>> callable = ignoringException(parse(value));
        return formats.tryPick(callable).
                getOrElse(callThrows(new IllegalArgumentException("Invalid date string: " + value), Date.class));
    }

    public static Callable1<DateFormat, Date> parse(final String value) {
        return new Callable1<DateFormat, Date>() {
            public Date call(DateFormat dateFormat) throws Exception {
                return dateFormat.parse(value);
            }
        };
    }
}
