package com.googlecode.totallylazy.time;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequence;

import java.text.DateFormat;
import java.util.Date;

import static com.googlecode.totallylazy.Callables.callThrows;
import static com.googlecode.totallylazy.Exceptions.ignoringException;
import static com.googlecode.totallylazy.Sequences.sequence;

public class DateFormatConverter implements DateConverter {
    private final Sequence<DateFormat> formats;

    public DateFormatConverter(String... formats) {
        this(sequence(formats).map(asDateFormat()));
    }

    public static Function1<? super String, DateFormat> asDateFormat() {
        return new Function1<String, DateFormat>() {
            @Override
            public DateFormat call(String value) throws Exception {
                return Dates.format(value);
            }
        };
    }

    public Sequence<DateFormat> formats() {
        return formats;
    }

    public DateFormatConverter(DateFormat... formats) {
        this(sequence(formats));
    }

    public DateFormatConverter(Iterable<DateFormat> formats) {
        this.formats = sequence(formats);
        if(this.formats.isEmpty()) {
            throw new IllegalArgumentException("No format specified");
        }
    }

    public static DateFormatConverter defaultConverter() {
        return new DateFormatConverter(Dates.RFC3339().formats().join(sequence(Dates.RFC822(), Dates.javaUtilDateToString(), Dates.LUCENE())));
    }

    public String format(final Date value) {
        return formats.head().format(value);
    }

    public Date parse(final String value) {
        Callable1<DateFormat, Option<Date>> callable = ignoringException(parseToDate(value));
        return formats.tryPick(callable).
                getOrElse(callThrows(new IllegalArgumentException("Invalid date string: " + value), Date.class));
    }

    public static Callable1<DateFormat, Date> parseToDate(final String value) {
        return new Callable1<DateFormat, Date>() {
            public Date call(DateFormat dateFormat) throws Exception {
                return dateFormat.parse(value);
            }
        };
    }
}
