package com.googlecode.totallylazy.time;

import com.googlecode.totallylazy.CurriedFunction2;

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
        public static CurriedFunction2<Date, Integer, Date> add = new CurriedFunction2<Date, Integer, Date>() {
            @Override
            public Date call(Date date, Integer amount) throws Exception {
                return Hours.add(date, amount);
            }
        };

        public static CurriedFunction2<Date, Integer, Date> add()  {
            return add;
        }

        public static CurriedFunction2<Date, Integer, Date> subtract = new CurriedFunction2<Date, Integer, Date>() {
            @Override
            public Date call(Date date, Integer amount) throws Exception {
                return Hours.subtract(date, amount);
            }
        };

        public static CurriedFunction2<Date, Integer, Date> subtract()  {
            return subtract;
        }

        public static CurriedFunction2<Date, Date, Long> between = new CurriedFunction2<Date, Date, Long>() {
            @Override
            public Long call(Date start, Date end) throws Exception {
                return Hours.between(start, end);
            }
        };

        public static CurriedFunction2<Date, Date, Long> between()  {
            return between;
        }
    }
}