package com.googlecode.totallylazy.records;

import org.junit.Test;

import static com.googlecode.totallylazy.records.MapRecord.record;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MapRecordTest {
    private Keyword<String> firstName = Keywords.keyword("firstName", String.class);

    @Test
    public void supportsEquality() throws Exception {
        Record dan = record().set(firstName, "dan");
        Record dansDouble = record().set(firstName, "dan");
        Record mat = record().set(firstName, "mat");
        Record nullName = record().set(firstName, null);

        assertThat(dan.equals(dansDouble), is(true));
        assertThat(nullName.equals(nullName), is(true));
        assertThat(dan.equals(mat), is(false));
        assertThat(dan.equals(nullName), is(false));
    }


}
