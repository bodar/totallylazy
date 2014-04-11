package com.googlecode.totallylazy.time;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Seq;

import java.text.DateFormat;
import java.util.Date;

import static com.googlecode.totallylazy.Callables.callThrows;
import static com.googlecode.totallylazy.Sequences.sequence;

public class DateFormatConverter implements DateConverter {
    private final Seq<DateFormat> formats;

    public DateFormatConverter(String... formats) {
        this(sequence(formats).map(asDateFormat()));
    }

    public static Function<? super String, DateFormat> asDateFormat() {
        return new Function<String, DateFormat>() {
            @Override
            public DateFormat call(String value) throws Exception {
                return Dates.format(value);
            }
        };
    }

    public Seq<DateFormat> formats() {
        return formats;
    }

    public DateFormatConverter(DateFormat... formats) {
        this(sequence(formats));
    }

    public DateFormatConverter(Iterable<DateFormat> formats) {
        this.formats = sequence(formats);
        if (this.formats.isEmpty()) {
            throw new IllegalArgumentException("No format specified");
        }
    }

    public static DateFormatConverter defaultConverter() {
        return new DateFormatConverter(Dates.RFC3339().formats().join(sequence(Dates.RFC822(), Dates.javaUtilDateToString(), Dates.LEXICAL(), Dates.APACHE())));
    }

    public String format(final Date value) {
        return formats.head().format(value);
    }

    public Date parse(final String value) {
        return formats.tryPick(parseToDate(value).optional()).
                getOrElse(callThrows(new IllegalArgumentException("Invalid date string: " + value), Date.class));
    }

    public static Function<DateFormat, Date> parseToDate(final String value) {
        return new Function<DateFormat, Date>() {
            public Date call(DateFormat dateFormat) throws Exception {
                return dateFormat.parse(value);
            }
        };
    }
}
