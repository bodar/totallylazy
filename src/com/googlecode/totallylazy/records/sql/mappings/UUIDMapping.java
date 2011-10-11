package com.googlecode.totallylazy.records.sql.mappings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UUIDMapping implements Mapping<UUID>{
    public UUID getValue(ResultSet resultSet, Integer index) throws SQLException {
        return UUID.fromString(resultSet.getString(index));
    }

    public void setValue(PreparedStatement statement, Integer index, UUID value) throws SQLException {
        statement.setString(index, value.toString());
    }
}
