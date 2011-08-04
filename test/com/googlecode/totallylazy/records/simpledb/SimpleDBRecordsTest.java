package com.googlecode.totallylazy.records.simpledb;

import com.amazonaws.auth.BasicAWSCredentials;
import com.googlecode.totallylazy.records.AbstractRecordsTests;

import static java.lang.System.getenv;

public class SimpleDBRecordsTest extends AbstractRecordsTests<SimpleDBRecords>{
    @Override
    protected SimpleDBRecords createRecords() throws Exception {
        return new SimpleDBRecords(new BasicAWSCredentials(getenv("AMAZON_ACCESS_KEY"), getenv("AMAZON_SECRET_KEY")));
    }
}
