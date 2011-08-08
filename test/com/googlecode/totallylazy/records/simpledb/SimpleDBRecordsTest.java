package com.googlecode.totallylazy.records.simpledb;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.googlecode.totallylazy.records.AbstractRecordsTests;
import com.googlecode.totallylazy.records.simpledb.mappings.Mappings;
import org.junit.Ignore;
import org.junit.Test;

import java.io.InputStream;

import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.matchers.NumberMatcher.is;
import static com.googlecode.totallylazy.records.MapRecord.record;
import static org.hamcrest.MatcherAssert.assertThat;

@Ignore("Manual test")
public class SimpleDBRecordsTest extends AbstractRecordsTests<SimpleDBRecords>{
    @Override
    protected SimpleDBRecords createRecords() throws Exception {
        InputStream credentials = getClass().getResourceAsStream("AwsCredentials.properties");
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        return new SimpleDBRecords(new AmazonSimpleDBClient(new PropertiesCredentials(credentials), new ClientConfiguration().withMaxErrorRetry(5)), true, new Mappings(), logger);
    }

    @Override
    @Ignore("Not Supported by AWS")
    public void supportsAliasingAKeyword() throws Exception {
    }

    @Test
    public void canAddMoreThat25RecordsAtATimeAndReceiveMoreThanAHundred() throws Exception {
        records.add(books, repeat(record().set(isbn, zenIsbn)).take(100));
        assertThat(records.get(books).size(), is(103));
    }
}
