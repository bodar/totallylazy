package com.googlecode.totallylazy.time;

import com.googlecode.totallylazy.Function2;

import java.util.Date;

import static java.util.Calendar.SECOND;

public class Seconds {
    public static Date add(Date date, int amount) {
        return Dates.add(date, SECOND, amount);
    }

    public static Date subtract(Date date, int amount) {
        return Dates.subtract(date, SECOND, amount);
    }

    public static Long between(Date start, Date end) {
        return (end.getTime() - start.getTime()) / 1000L;
    }

    public static class functions {
        public static Function2<Date, Integer, Date> add = new Function2<Date, Integer, Date>() {
            @Override
            public Date call(Date date, Integer amount) throws Exception {
                return Seconds.add(date, amount);
            }
        };

        public static Function2<Date, Integer, Date> add()  {
            return add;
        }

        public static Function2<Date, Integer, Date> subtract = new Function2<Date, Integer, Date>() {
            @Override
            public Date call(Date date, Integer amount) throws Exception {
                return Seconds.subtract(date, amount);
            }
        };

        public static Function2<Date, Integer, Date> subtract()  {
            return subtract;
        }

        public static Function2<Date, Date, Long> between = new Function2<Date, Date, Long>() {
            @Override
            public Long call(Date start, Date end) throws Exception {
                return Seconds.between(start, end);
            }
        };

        public static Function2<Date, Date, Long> between()  {
            return between;
        }
    }
}