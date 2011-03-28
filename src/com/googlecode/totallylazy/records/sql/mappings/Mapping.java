package com.googlecode.totallylazy.records.sql.mappings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Mapping<T> {
    T getValue(ResultSet resultSet, Integer index) throws SQLException;

    void setValue(PreparedStatement statement, Integer index, T instance) throws SQLException;
}
