package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.records.Transaction;

import java.sql.Connection;
import java.sql.SQLException;

public class SqlTransaction implements Transaction{
    private final Connection connection;

    public SqlTransaction(Connection connection) {
        this.connection = connection;
        try {
            // Start transaction
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new LazyException("Could not begin transaction", e);
        }
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new LazyException("Could not commit transaction", e);
        }
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new LazyException("Could not rollback transaction", e);
        }
    }
}

