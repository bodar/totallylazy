package com.googlecode.totallylazy.time;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.BiFunction;

import java.util.Date;

public interface DateConverter {
    String format(Date value);

    Date parse(String value);

    public static class functions {
        public static BiFunction<DateConverter, Date, String> format = (dateConverter, date) -> dateConverter.format(date);

        public static BiFunction<DateConverter, Date, String> format()  {
            return format;
        }

        public static Function<Date, String> format(DateConverter dateConverter)  {
            return format.apply(dateConverter);
        }

        public static BiFunction<DateConverter, String, Date> parse = new BiFunction<DateConverter, String, Date>() {
            @Override
            public Date call(DateConverter dateConverter, String dateAsString) throws Exception {
                return dateConverter.parse(dateAsString);
            }
        };

        public static BiFunction<DateConverter, String, Date> parse() {
            return parse;
        }

        public static Function<String, Date> parse(DateConverter dateConverter)  {
            return parse.apply(dateConverter);
        }
    }

}
