package com.googlecode.totallylazy.json;

import com.googlecode.totallylazy.Randoms;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.io.Uri;
import com.googlecode.totallylazy.time.Dates;
import org.junit.Test;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import static com.googlecode.totallylazy.Lists.list;
import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Strings.string;
import static com.googlecode.totallylazy.time.Dates.date;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class JsonTest {
    @Test
    public void supportsStreaming() throws Exception {
        assertThat(Json.<String>pairs(new StringReader("{\"root\" : \"text\"}")).head(), is(pair("root", "text")));
        assertThat(Json.<String>sequence(new StringReader("[\"one\", \"two\"]")).head(), is("one"));
    }

    @Test
    public void supportsParsingToVariousNativeJavaTypes() throws Exception {
        assertThat(Json.<String>map(("{\"root\" : \"text\"}")).get("root"), is("text"));
        assertThat(Json.<String>list(("[\"one\", \"two\"]")).get(0), is("one"));
        assertThat(Json.object(("123")), instanceOf(Number.class));
    }

    @Test
    public void correctlyParsesASingleRootElement() throws Exception {
        Map<String, Object> result = Json.map(("{\"root\" : \"text\"}"));

        assertThat((String) result.get("root"), is("text"));
    }

    @Test
    public void correctlyRendersASingleRootElement() throws Exception {
        String result = Json.json(map("root", "text"));

        assertThat(result,
                is("{\"root\":\"text\"}"));
    }

    @Test
    public void writesDatesInIsoFormat() throws Exception {
        Date date = date(1977, 1, 10, 23, 05, 33, 123);
        assertThat(Json.json(map("dob", date)),
                is("{\"dob\":\"" + Dates.RFC3339withMilliseconds().format(date) + "\"}"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void correctlyRendersAndParsesIntegersAndText() throws Exception {
        Map<String, ?> model = map("root",
                map("child", list(
                        BigDecimal.valueOf(1),
                        BigDecimal.valueOf(-5),
                        "text")));

        String json = Json.json(model);

        assertThat(json,
                is("{\"root\":{\"child\":[1,-5,\"text\"]}}"));

        assertThat(Json.map(json), is(Unchecked.<Map<String, Object>>cast(model)));
    }

    @Test
    public void handlesOtherDataType() throws Exception {
        String json = Json.json(map("uri", Uri.uri("http://code.google.com/p/funclate/")));

        assertThat(json, is("{\"uri\":\"http://code.google.com/p/funclate/\"}"));
    }

    @Test
    public void handlesNumbers() throws Exception {
        Integer number = Randoms.integers().head();

        String json = Json.json(map("number", number));

        assertThat(json, is("{\"number\":" + number + "}"));
    }

    @Test
    public void handlesBooleans() throws Exception {
        assertThat(Json.json(map("Boolean", Boolean.TRUE)), is("{\"Boolean\":true}"));
        assertThat(Json.json(map("boolean", false)), is("{\"boolean\":false}"));
    }

    @Test
    public void correctlyRendersAModel() throws Exception {
        String result = Json.json(map("root", map("foo", list("bar", map("baz", list(1, 2))))));

        assertThat(result,
                is("{\"root\":{\"foo\":[\"bar\",{\"baz\":[1,2]}]}}"));
    }

    @Test
    public void shouldPreserveNewLineCharacters() throws Exception {
        String result = Json.json(map("text", "this is \\n a test"));
        Map<String, Object> parsed = Json.map(result);
        assertThat((String) parsed.get("text"), is("this is \\n a test"));
    }


    @Test
    public void handlesQuotedText() throws Exception {
        String result = Json.json(map("text", "He said \"Hello\" then ..."));

        assertThat(result, is("{\"text\":\"He said \\\"Hello\\\" then ...\"}"));

        Map<String, Object> parsed = Json.map(result);
        assertThat((String) parsed.get("text"), is("He said \"Hello\" then ..."));
    }

    @Test
    public void handlesSpecialCharacters() throws Exception {
        String result = Json.json(map("text", "first line\n second line λ"));

        assertThat(result, is("{\"text\":\"first line\\n second line λ\"}"));

        Map<String, Object> parsed = Json.map(result);
        assertThat((String) parsed.get("text"), is("first line\n second line λ"));

        Map<String, Object> parsedWithUnicode = Json.map("{\"text\":\"first line\\n second line \\u03BB\"}");
        assertThat((String) parsedWithUnicode.get("text"), is("first line\n second line λ"));
    }

    @Test
    public void handlesLowercaseHexCodes() throws Exception {
        Map<String, Object> lowerCaseUnicode = Json.map("{ \"text\": \"\\u00e7\" }");
        assertThat((String) lowerCaseUnicode.get("text"), is("ç"));
    }

    @Test
    public void handlesNulls() throws Exception {
        assertThat(Json.json(map("nullValue", null)), is("{\"nullValue\":null}"));
    }
}
