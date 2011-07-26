package com.googlecode.totallylazy.records.sql.mappings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IntegerMapping implements Mapping<Integer> {
    public Integer getValue(ResultSet resultSet, Integer index) throws SQLException {
        int result = resultSet.getInt(index);
        if (resultSet.wasNull()) {
            return null;
        }
        return result;
    }

    public void setValue(PreparedStatement statement, Integer index, Integer value) throws SQLException {
        statement.setInt(index, value);
    }

    public String type() {
        return "integer";
    }
}
