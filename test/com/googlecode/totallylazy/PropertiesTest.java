package com.googlecode.totallylazy;

import junit.framework.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Properties.compose;
import static com.googlecode.totallylazy.Properties.copy;
import static com.googlecode.totallylazy.Properties.properties;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.PredicateAssert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;

public class PropertiesTest {
    @Test
    public void composesTwoPropertyFiles() {
        java.util.Properties result = compose(properties("a=1\nb=SHOULD_BE_OVERRIDEN"), properties("b=2\nc=3"));

        assertThat(result.getProperty("a"), is("1"));
        assertThat(result.getProperty("b"), is("2"));
        assertThat(result.getProperty("c"), is("3"));
    }

    @Test
    public void providesFunctionAllowingCompositionOfPropertiesByFolding() {
        java.util.Properties result = sequence(
                properties("a=SHOULD_BE_OVERRIDEN\nb=SHOULD_BE_OVERRIDEN"),
                properties("b=2\nc=3"),
                properties("a=1"))
                .fold(new java.util.Properties(), compose());

        assertThat(result.getProperty("a"), is("1"));
        assertThat(result.getProperty("b"), is("2"));
        assertThat(result.getProperty("c"), is("3"));
    }

    @Test
    public void doesntThrowIfThePropertyValueIsVeryLong() {
        java.util.Properties result = compose(properties(String.format("a=1\nb=%s", veryLongString())));
        assertThat(result.getProperty("b"), is(veryLongString()));
    }

    @Test
    public void loadsPropertiesFromAnInputStream() throws IOException {
        java.util.Properties properties = properties(new ByteArrayInputStream("a=1\nb=2".getBytes()));

        assertThat(properties.getProperty("a"), is("1"));
        assertThat(properties.getProperty("b"), is("2"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void createsPropertiesFromPairs() {
        java.util.Properties fromVarargs = properties(pair("a", "1"), pair("b", "2"));

        assertThat(fromVarargs.getProperty("a"), is("1"));
        assertThat(fromVarargs.getProperty("b"), is("2"));

        java.util.Properties fromSequence = properties(sequence(pair("a", "1"), pair("b", "2")));

        assertThat(fromSequence.getProperty("a"), is("1"));
        assertThat(fromSequence.getProperty("b"), is("2"));
    }

    @Test
    public void copiesProperties() {
        java.util.Properties original = properties("a=1\nb=2");
        java.util.Properties copy = copy(original);

        original.setProperty("a", "9999");

        assertThat(copy.getProperty("a"), is("1"));
    }

    @Test
    public void convertsPropertiesToAString() {
        java.util.Properties original = properties("a=1\nb=2");

        java.util.Properties toStringAndBackAgain = properties(Properties.asString(original));

        assertThat(toStringAndBackAgain.size(), is(2));
        assertThat(toStringAndBackAgain.getProperty("a"), is("1"));
        assertThat(toStringAndBackAgain.getProperty("b"), is("2"));
    }

    @Test
    public void providesAFunctionToCreatePropertiesFromAString() throws Exception {
        java.util.Properties properties = Properties.propertiesFromString().call("a=1\nb=2");

        assertThat(properties.getProperty("a"), is("1"));
        assertThat(properties.getProperty("b"), is("2"));
    }

    @Test
    public void guaranteesPropertiesExist() {
        try {
            Properties.expectProperty(new java.util.Properties(), "NOT THERE");
            Assert.fail("Expected exception");
        } catch (NoSuchElementException e) {
            assertThat(e.getMessage(), Strings.contains("NOT THERE"));
        }
    }

    private String veryLongString() {
        String value = "";
        for (int i = 0; i < 2000; i++)
            value += "x";
        return value;
    }
}
