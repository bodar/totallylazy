package com.googlecode.totallylazy.time;

import com.googlecode.totallylazy.Curried2;

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
        public static Curried2<Date, Integer, Date> add = new Curried2<Date, Integer, Date>() {
            @Override
            public Date call(Date date, Integer amount) throws Exception {
                return Seconds.add(date, amount);
            }
        };

        public static Curried2<Date, Integer, Date> add()  {
            return add;
        }

        public static Curried2<Date, Integer, Date> subtract = new Curried2<Date, Integer, Date>() {
            @Override
            public Date call(Date date, Integer amount) throws Exception {
                return Seconds.subtract(date, amount);
            }
        };

        public static Curried2<Date, Integer, Date> subtract()  {
            return subtract;
        }

        public static Curried2<Date, Date, Long> between = new Curried2<Date, Date, Long>() {
            @Override
            public Long call(Date start, Date end) throws Exception {
                return Seconds.between(start, end);
            }
        };

        public static Curried2<Date, Date, Long> between()  {
            return between;
        }
    }
}