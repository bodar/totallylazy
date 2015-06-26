package com.googlecode.totallylazy.time;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.Sequence;

import java.text.DateFormat;
import java.util.Date;

import static com.googlecode.totallylazy.functions.Callables.callThrows;
import static com.googlecode.totallylazy.Sequences.sequence;

public class DateFormatConverter implements DateConverter {
    private final Sequence<DateFormat> formats;

    public DateFormatConverter(String... formats) {
        this(sequence(formats).map(asDateFormat()));
    }

    public static Function1<? super String, DateFormat> asDateFormat() {
        return Dates::format;
    }

    public Sequence<DateFormat> formats() {
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

    public static Function1<DateFormat, Date> parseToDate(final String value) {
        return dateFormat -> dateFormat.parse(value);
    }
}
