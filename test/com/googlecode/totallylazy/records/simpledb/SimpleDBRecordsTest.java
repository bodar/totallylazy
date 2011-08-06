package com.googlecode.totallylazy.records.simpledb;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.googlecode.totallylazy.records.AbstractRecordsTests;
import org.junit.Ignore;

import static java.lang.System.getenv;

@Ignore
public class SimpleDBRecordsTest extends AbstractRecordsTests<SimpleDBRecords>{
    @Override
    protected SimpleDBRecords createRecords() throws Exception {
        final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(getenv("AMAZON_ACCESS_KEY"), getenv("AMAZON_SECRET_KEY"));
        return new SimpleDBRecords(new AmazonSimpleDBClient(basicAWSCredentials, new ClientConfiguration().withMaxErrorRetry(5)));
    }

    @Override
    @Ignore("Not Supported by AWS")
    public void supportsAliasingAKeyword() throws Exception {
    }
}
