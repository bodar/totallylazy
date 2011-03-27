package com.googlecode.totallylazy.records.sql.mappings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LongMapping implements Mapping<Long> {
    public Long getValue(ResultSet resultSet, Integer index) throws SQLException {
        long result = resultSet.getLong(index);
        if (resultSet.wasNull()) {
            return null;
        }
        return result;
    }

    public void setValue(PreparedStatement statement, Integer index, Long value) throws SQLException {
        statement.setLong(index, value);
    }
}
