package com.googlecode.totallylazy.records.sql.mappings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BooleanMapping implements Mapping<Boolean>{
    public Boolean getValue(ResultSet resultSet, Integer index) throws SQLException {
        return resultSet.getBoolean(index);
    }

    public void setValue(PreparedStatement statement, Integer index, Boolean value) throws SQLException {
        statement.setBoolean(index, value);
    }
}
