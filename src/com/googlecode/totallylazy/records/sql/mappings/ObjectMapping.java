package com.googlecode.totallylazy.records.sql.mappings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjectMapping implements Mapping<Object>{
    public Object getValue(ResultSet resultSet, Integer index) throws SQLException {
        return resultSet.getObject(index);
    }

    public void setValue(PreparedStatement statement, Integer index, Object value) throws SQLException {
        statement.setObject(index, value);
    }
}
