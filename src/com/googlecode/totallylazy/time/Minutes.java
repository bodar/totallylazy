package com.googlecode.totallylazy.time;

import com.googlecode.totallylazy.BiFunction;

import java.util.Date;

import static java.util.Calendar.MINUTE;

public class Minutes {
    public static Date add(Date date, int amount) {
        return Dates.add(date, MINUTE, amount);
    }

    public static Date subtract(Date date, int amount) {
        return Dates.subtract(date, MINUTE, amount);
    }

    public static Long between(Date start, Date end) {
        return Seconds.between(start, end) / 60;
    }

    public static class functions {
        public static BiFunction<Date, Integer, Date> add = new BiFunction<Date, Integer, Date>() {
            @Override
            public Date call(Date date, Integer amount) throws Exception {
                return Minutes.add(date, amount);
            }
        };

        public static BiFunction<Date, Integer, Date> add()  {
            return add;
        }

        public static BiFunction<Date, Integer, Date> subtract = new BiFunction<Date, Integer, Date>() {
            @Override
            public Date call(Date date, Integer amount) throws Exception {
                return Minutes.subtract(date, amount);
            }
        };

        public static BiFunction<Date, Integer, Date> subtract()  {
            return subtract;
        }

        public static BiFunction<Date, Date, Long> between = new BiFunction<Date, Date, Long>() {
            @Override
            public Long call(Date start, Date end) throws Exception {
                return Minutes.between(start, end);
            }
        };

        public static BiFunction<Date, Date, Long> between()  {
            return between;
        }
    }
}