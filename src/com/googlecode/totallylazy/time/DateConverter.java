package com.googlecode.totallylazy.time;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.functions.CurriedFunction2;

import java.util.Date;

public interface DateConverter {
    String format(Date value);

    Date parse(String value);

    class functions {
        public static CurriedFunction2<DateConverter, Date, String> format = DateConverter::format;

        public static CurriedFunction2<DateConverter, Date, String> format()  {
            return format;
        }

        public static Function1<Date, String> format(DateConverter dateConverter)  {
            return format.apply(dateConverter);
        }

        public static CurriedFunction2<DateConverter, String, Date> parse = DateConverter::parse;

        public static CurriedFunction2<DateConverter, String, Date> parse() {
            return parse;
        }

        public static Function1<String, Date> parse(DateConverter dateConverter)  {
            return parse.apply(dateConverter);
        }
    }

}
