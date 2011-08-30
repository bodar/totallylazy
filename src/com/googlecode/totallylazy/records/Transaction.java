package com.googlecode.totallylazy.records;

public interface Transaction {
    void commit();
    void rollback();
}
