package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.records.Keyword;
import org.junit.Test;

import static com.googlecode.totallylazy.records.Keywords.keyword;
import static com.googlecode.totallylazy.records.sql.expressions.SelectBuilder.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SelectBuilderTest {
    private final Keyword cars = keyword("cars");
    private final Keyword<String> make = keyword("make", String.class);

    @Test
    public void canSelect() throws Exception {
        SelectExpression build = from(cars).select(make).build();
        assertThat(build.text(), is("select all make from cars"));
    }
}
