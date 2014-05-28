package com.googlecode.totallylazy;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.Properties;

import static com.googlecode.totallylazy.PredicateAssert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;

public class PrefixPropertiesTest {
    @Test
    public void supportsGet() throws Exception {
        PrefixProperties properties = new PrefixProperties("library.book", properties());
        assertThat(properties.getProperty("urn"), is("urn:isbn:0099322617"));
        assertThat(properties.getProperty("missing", "default"), is("default"));
    }

    @Test
    public void supportsSet() throws Exception {
        PrefixProperties properties = new PrefixProperties("library.book", properties());
        properties.setProperty("title", "Rubbish");
        assertThat(properties.getProperty("title"), is("Rubbish"));
    }

    @Test
    public void supportsLoad() throws Exception {
        PrefixProperties properties = new PrefixProperties("library.book");
        properties.load(new StringReader("library.book.title=Zen And The Art Of Motorcycle Maintenance"));
        assertThat(properties.getProperty("title"), is("Zen And The Art Of Motorcycle Maintenance"));
    }

    @Test
    public void supportsStore() throws Exception {
        PrefixProperties properties = new PrefixProperties("library.book");
        properties.setProperty("title", "Zen And The Art Of Motorcycle Maintenance");
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        properties.store(output, "");
        assertThat(output.toString(), Strings.contains("library.book.title=Zen And The Art Of Motorcycle Maintenance"));
    }

    private Properties properties() {
        Properties properties = new Properties();
        properties.setProperty("library.book.urn", "urn:isbn:0099322617");
        properties.setProperty("library.book.title", "Zen And The Art Of Motorcycle Maintenance");
        return properties;
    }
}
