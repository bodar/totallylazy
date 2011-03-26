package com.googlecode.totallylazy.records.sql.mappings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class LongMapping implements Mapping<Long> {
    public Long getValue(ResultSet resultSet, String name) throws SQLException {
        long result = resultSet.getLong(name);
        if (resultSet.wasNull()) {
            return null;
        }
        return result;
    }

    public void setValue(PreparedStatement statement, Integer index, Long value) throws SQLException {
        if (value == null) {
            statement.setObject(index, null);
        } else {
            statement.setLong(index, value);
        }
    }
}
