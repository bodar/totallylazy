package com.googlecode.totallylazy.time;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Curried2;

import java.util.Date;

public interface DateConverter {
    String format(Date value);

    Date parse(String value);

    public static class functions {
        public static Curried2<DateConverter, Date, String> format = new Curried2<DateConverter, Date, String>() {
            @Override
            public String call(DateConverter dateConverter, Date date) throws Exception {
                return dateConverter.format(date);
            }
        };

        public static Curried2<DateConverter, Date, String> format()  {
            return format;
        }

        public static Function1<Date, String> format(DateConverter dateConverter)  {
            return format.apply(dateConverter);
        }

        public static Curried2<DateConverter, String, Date> parse = new Curried2<DateConverter, String, Date>() {
            @Override
            public Date call(DateConverter dateConverter, String dateAsString) throws Exception {
                return dateConverter.parse(dateAsString);
            }
        };

        public static Curried2<DateConverter, String, Date> parse() {
            return parse;
        }

        public static Function1<String, Date> parse(DateConverter dateConverter)  {
            return parse.apply(dateConverter);
        }
    }

}
