package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.records.Keyword;
import org.junit.Test;

import static com.googlecode.totallylazy.records.Keywords.keyword;
import static com.googlecode.totallylazy.records.sql.expressions.SelectBuilder.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SelectBuilderTest {
    private final Keyword cars = keyword("cars");
    private final Keyword<String> make = keyword("make", String.class);
    private final Keyword<String> model = keyword("model", String.class);
    private final Keyword<Integer> one = keyword("1", Integer.class);

    @Test
    public void ifColumnsSelectedIsEmptyUseStar() throws Exception {
        SelectExpression build = from(cars).select(Sequences.<Keyword>empty()).build();
        assertThat(build.text(), is("select * from cars"));
    }

    @Test
    public void selectASingleColumn() throws Exception {
        SelectExpression build = from(cars).select(make).build();
        assertThat(build.text(), is("select make from cars"));
    }

    @Test
    public void selectMultipleColumns() throws Exception {
        SelectExpression build = from(cars).select(make, model).build();
        assertThat(build.text(), is("select make,model from cars"));
    }

    @Test
    public void canBeUsedToTestForATable() throws Exception {
        SelectExpression build = from(cars).select(one).build();
        assertThat(build.text(), is("select 1 from cars"));
    }
}
