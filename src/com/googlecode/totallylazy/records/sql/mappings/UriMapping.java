package com.googlecode.totallylazy.records.sql.mappings;

import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.googlecode.totallylazy.URLs.uri;

public class UriMapping implements Mapping<URI>{
    private final StringMapping mapping = new StringMapping();
    public URI getValue(ResultSet resultSet, Integer index) throws SQLException {
        String value = mapping.getValue(resultSet, index);
        return value == null ? null : uri(value);
    }

    public void setValue(PreparedStatement statement, Integer index, URI value) throws SQLException {
        mapping.setValue(statement, index, value == null ? null : value.toString());
    }
}
