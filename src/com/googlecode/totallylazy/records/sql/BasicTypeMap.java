package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Keyword;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.numbers.Numbers.increment;

public class BasicTypeMap {
    public static Object getValue(final ResultSet resultSet, final Keyword keyword) throws SQLException {
        String name = keyword.name();
        Class aClass = keyword.forClass();
        if (aClass.equals(Date.class)) {
            return new Date(resultSet.getTimestamp(name).getTime());
        }
        if (aClass.equals(Timestamp.class)) {
            return resultSet.getTimestamp(name);
        }
        if (aClass.equals(String.class)) {
            return resultSet.getString(name);
        }
        if (aClass.equals(Integer.class)) {
            if (resultSet.getObject(name) == null) {
                return null;
            }
            return resultSet.getInt(name);
        }
        if (aClass.equals(Long.class)) {
            if (resultSet.getObject(name) == null) {
                return null;
            }
            return resultSet.getLong(name);
        }
        if(aClass.equals(Object.class)){
            return resultSet.getObject(name);
        }
        throw new UnsupportedOperationException();
    }

    public static void addValues(PreparedStatement statement, Sequence<Object> values) throws SQLException {
        for (Pair<Integer, Object> pair : iterate(increment(), 1).safeCast(Integer.class).zip(values)) {
            Integer index = pair.first();
            Object value = pair.second();
            if (value instanceof Date) {
                Date date = (Date) value;
                statement.setTimestamp(index, new Timestamp(date.getTime()));
            } else {
                statement.setObject(index, value);
            }
        }
    }
}
