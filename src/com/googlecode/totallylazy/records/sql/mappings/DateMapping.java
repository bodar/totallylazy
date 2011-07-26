package com.googlecode.totallylazy.records.sql.mappings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class DateMapping implements Mapping<Date>{
    public Date getValue(ResultSet resultSet, Integer index) throws SQLException {
        return new Date(resultSet.getTimestamp(index).getTime());
    }

    public void setValue(PreparedStatement statement, Integer index, Date date) throws SQLException {
        statement.setTimestamp(index, new Timestamp(date.getTime()));
    }

    public String type() {
        return "timestamp";
    }
}
