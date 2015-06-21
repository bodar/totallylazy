package com.googlecode.totallylazy.collections;

import org.junit.Test;

import java.util.Date;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;

public class KeywordTest {
    @Test
    public void canDoReflectoMagicToDetermineNameAndClassOfLocalVariables() throws Exception {
        Keyword<Date> dob = Keyword.keyword();
        assertThat(dob.name(), is("dob"));
        assertThat(dob.forClass(), is(Date.class));
        Keyword<String> name = Keyword.keyword();
        assertThat(name.name(), is("name"));
        assertThat(name.forClass(), is(String.class));
    }

    @Test
    public void supportsFields() throws Exception {
        class Foo {
            Keyword<Date> dob = Keyword.keyword();
            Keyword<String> name = Keyword.keyword();
        }

        Foo foo = new Foo();
        Keyword<Date> dob = foo.dob;
        assertThat(dob.name(), is("dob"));
        assertThat(dob.forClass(), is(Date.class));
        Keyword<String> name = foo.name;
        assertThat(name.name(), is("name"));
        assertThat(name.forClass(), is(String.class));

    }
}


