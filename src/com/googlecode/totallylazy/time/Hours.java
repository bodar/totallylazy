package com.googlecode.totallylazy.time;

import com.googlecode.totallylazy.functions.Curried2;

import java.util.Date;

import static java.util.Calendar.HOUR;

public class Hours {
    public static Date add(Date date, int amount) {
        return Dates.add(date, HOUR, amount);
    }

    public static Date subtract(Date date, int amount) {
        return Dates.subtract(date, HOUR, amount);
    }

    public static Long between(Date start, Date end) {
        return Minutes.between(start, end) / 60;
    }

    public static class functions {
        public static Curried2<Date, Integer, Date> add = (date, amount) -> Hours.add(date, amount);

        public static Curried2<Date, Integer, Date> add()  {
            return add;
        }

        public static Curried2<Date, Integer, Date> subtract = (date, amount) -> Hours.subtract(date, amount);

        public static Curried2<Date, Integer, Date> subtract()  {
            return subtract;
        }

        public static Curried2<Date, Date, Long> between = (start, end) -> Hours.between(start, end);

        public static Curried2<Date, Date, Long> between()  {
            return between;
        }
    }
}