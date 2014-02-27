package com.googlecode.totallylazy.json;

import com.googlecode.totallylazy.Sequence;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.list;
import static com.googlecode.totallylazy.collections.PersistentSortedMap.constructors.sortedMap;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.junit.Assert.assertThat;

public class JsonWriterTest {
    @Test
    public void canStreamAnIterator() throws Exception {
        Sequence<String> values = sequence("1", "2");
        Writer writer = new StringWriter();

        JsonWriter.write(values.iterator(), writer);

        String actual = writer.toString();
        assertThat(actual, is("[\"1\",\"2\"]"));
        assertThat(actual, is(Json.json(values)));
    }

    @Test
    public void canStreamANestedStructure() throws Exception {
        Map<String, ?> model =
                sortedMap("root",
                        sortedMap("parent",
                                sortedMap("children",
                                        list("1", true))));

        Writer writer = new StringWriter();

        JsonWriter.write(model, writer);

        String actual = writer.toString();
        assertThat(actual, is("{\"root\":{\"parent\":{\"children\":[\"1\",true]}}}"));
        assertThat(actual, is(Json.json(model)));
    }

    @Test
    public void canStreamAnIterable() throws Exception {
        Sequence<String> values = sequence("1", "2");
        Writer writer = new StringWriter();

        JsonWriter.write(values, writer);

        String actual = writer.toString();
        assertThat(actual, is("[\"1\",\"2\"]"));
        assertThat(actual, is(Json.json(values)));
    }

    @Test
    public void canStreamAMap() throws Exception {
        Map<String, Integer> values = sortedMap("one", 1, "two", 2);
        Writer writer = new StringWriter();

        JsonWriter.write(values, writer);

        String actual = writer.toString();
        assertThat(actual, is("{\"one\":1,\"two\":2}"));
        assertThat(actual, is(Json.json(values)));
    }

    @Test
    public void canStreamAMapEvenIfCastToAnIterable() throws Exception {
        Map<String, Integer> values = sortedMap("one", 1, "two", 2);
        Writer writer = new StringWriter();

        JsonWriter.write((Iterable) values, writer);

        String actual = writer.toString();
        assertThat(actual, is("{\"one\":1,\"two\":2}"));
        assertThat(actual, is(Json.json(values)));
    }
}

