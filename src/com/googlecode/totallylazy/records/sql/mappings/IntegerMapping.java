package com.googlecode.totallylazy.records.sql.mappings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class IntegerMapping implements Mapping<Integer>{
    public Integer getValue(ResultSet resultSet, String name) throws SQLException {
        int result = resultSet.getInt(name);
        if(resultSet.wasNull()){
            return null;
        }
        return result;
    }

    public void setValue(PreparedStatement statement, Integer index, Integer value) throws SQLException {
        if(value == null){
            statement.setNull(index, Types.INTEGER);
        } else {
            statement.setInt(index, value);
        }
    }
}
