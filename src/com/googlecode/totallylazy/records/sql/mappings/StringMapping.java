package com.googlecode.totallylazy.records.sql.mappings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class StringMapping implements Mapping<String>{
    public String getValue(ResultSet resultSet, Integer index) throws SQLException {
        return resultSet.getString(index);
    }

    public void setValue(PreparedStatement statement, Integer index, String value) throws SQLException {
        statement.setString(index, value);
    }
}
