package com.googlecode.totallylazy;

import org.junit.Test;

import java.util.Formatter;

import static com.googlecode.totallylazy.Enums.name;
import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterablePredicates.hasExactly;

public class EnumsTest {
    @Test
    public void canMapName() throws Exception {
        assertThat(sequence(Formatter.BigDecimalLayoutForm.DECIMAL_FLOAT).map(name(Formatter.BigDecimalLayoutForm.class)), hasExactly("DECIMAL_FLOAT"));
    }

}
