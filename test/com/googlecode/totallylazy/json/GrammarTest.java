package com.googlecode.totallylazy.json;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.callables.TimeReport;
import com.googlecode.totallylazy.matchers.NumberMatcher;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class GrammarTest {
    @Test
    public void canParseNull() throws Exception {
        assertThat(Grammar.NULL.parse("null").value(), is(nullValue()));
        assertThat(Grammar.NULL.parse("falure").failure(), is(true));
    }

    @Test
    public void canParseABoolean() throws Exception {
        assertThat(Grammar.BOOLEAN.parse("true").value(), is(true));
        assertThat(Grammar.BOOLEAN.parse("false").value(), is(false));
        assertThat(Grammar.BOOLEAN.parse("falure").failure(), is(true));
    }

    @Test
    public void canHandleEscapedCharacters() throws Exception {
        assertThat(Grammar.ESCAPED_CHARACTER.parse("\\\"").value(), is("\""));
        assertThat(Grammar.ESCAPED_CHARACTER.parse("\\\\").value(), is("\\"));
        assertThat(Grammar.ESCAPED_CHARACTER.parse("\\/").value(), is("/"));
        assertThat(Grammar.ESCAPED_CHARACTER.parse("\\b").value(), is("\b"));
        assertThat(Grammar.ESCAPED_CHARACTER.parse("\\f").value(), is("\f"));
        assertThat(Grammar.ESCAPED_CHARACTER.parse("\\n").value(), is("\n"));
        assertThat(Grammar.ESCAPED_CHARACTER.parse("\\r").value(), is("\r"));
        assertThat(Grammar.ESCAPED_CHARACTER.parse("\\t").value(), is("\t"));
        assertThat(Grammar.ESCAPED_CHARACTER.parse("\\u03BB").value(), is("λ"));
        assertThat(Grammar.ESCAPED_CHARACTER.parse("falure").failure(), is(true));
    }

    @Test
    public void canParseString() throws Exception {
        assertThat(Grammar.STRING.parse("\"\"").value(), is(""));
        assertThat(Grammar.STRING.parse("\"Word\"").value(), is("Word"));
        assertThat(Grammar.STRING.parse("\"This is some \\\" random string\"").value(), is("This is some \" random string"));
        assertThat(Grammar.STRING.parse("\"Text with unicode \\u03BB\"").value(), is("Text with unicode λ"));
        assertThat(Grammar.STRING.parse("falure").failure(), is(true));
    }

    @Test
    public void canParseNumber() throws Exception {
        assertThat(Grammar.NUMBER.parse("12").value(), NumberMatcher.is(12));
        assertThat(Grammar.NUMBER.parse("12.1").value(), NumberMatcher.is(12.1));
        assertThat(Grammar.NUMBER.parse("-12").value(), NumberMatcher.is(-12));
    }

    @Test
    public void canParsePair() throws Exception {
        Pair<String, Object> pair = Grammar.PAIR.parse("\"foo\":\"value\"").value();
        assertThat(pair.first(), is("foo"));
        assertThat((String) pair.second(), is("value"));
        Pair<String, Object> parse = Grammar.PAIR.parse("\"foo\":123").value();
        assertThat(parse.first(), is("foo"));
        assertThat((Number) parse.second(), NumberMatcher.is(123));
    }

    @Test
    public void canParseArray() throws Exception {
        List<Object> listOfOne = Grammar.ARRAY.parse("[\"foo\"]").value();
        assertThat((String) listOfOne.get(0), is("foo"));
        List<Object> listOfTwo = Grammar.ARRAY.parse("[\"foo\", 123 ]").value();
        assertThat((String) listOfTwo.get(0), is("foo"));
        assertThat((Number) listOfTwo.get(1), NumberMatcher.is(123));
        List<Object> empty = Grammar.ARRAY.parse("[]").value();
        assertThat(empty.isEmpty(), is(true));
    }


    @Test
    public void canParseObjectLiteral() throws Exception {
        Map<String, Object> mapOfOne = Grammar.OBJECT.parse("{ \"foo\" : 123 } ").value();
        assertThat((Number) mapOfOne.get("foo"), NumberMatcher.is(123));
        Map<String, Object> mapOfTwo = Grammar.OBJECT.parse("{\"foo\":123,\"bar\":\"baz\"}").value();
        assertThat((Number) mapOfTwo.get("foo"), NumberMatcher.is(123));
        assertThat((String) mapOfTwo.get("bar"), is("baz"));
        Map<String, Object> empty = Grammar.OBJECT.parse("{}").value();
        assertThat(empty.isEmpty(), is(true));
    }

    @Test
    public void canParseAValue() throws Exception {
        Number number = (Number) Grammar.VALUE.parse("1").value();
        assertThat(number, NumberMatcher.is(1));
        String string = (String) Grammar.VALUE.parse("\"foo\"").value();
        assertThat(string, is("foo"));
        Map map = (Map) Grammar.VALUE.parse("{\"foo\":123}").value();
        assertThat((Number) map.get("foo"), NumberMatcher.is(123));
        List array = (List) Grammar.VALUE.parse("[\"foo\",123]").value();
        assertThat(array.get(0), is((Object) "foo"));
        assertThat((Number) array.get(1), NumberMatcher.is(123));
    }

    @Test
    public void canParseNestedJson() throws Exception {
        Map map = (Map) Grammar.VALUE.parse(" { \"root\"  : { \"foo\" : [ \"bar\", { \"baz\" : [1, null, true, false, 12.3 ] } ] } }  ").value();
        Map root = (Map) map.get("root");
        List foo = (List) root.get("foo");
        assertThat(foo.get(0), is((Object) "bar"));
        Map child = (Map) foo.get(1);
        List baz = (List) child.get("baz");
        assertThat((Number)baz.get(0), NumberMatcher.is(1));
        assertThat(baz.get(1), is((nullValue())));
        assertThat(baz.get(2), is(((Object) true)));
        assertThat(baz.get(3), is(((Object) false)));
        assertThat((Number) baz.get(4), NumberMatcher.is(12.3));
    }

    @Test
    @Ignore("Manual test")
    public void performanceTest() throws Exception {
        final String json = "{\n" +
                "    \"graph\": {\n" +
                "        \"mode\":\"NORMAL\",\n" +
                "        \"vertices\": [\n" +
                "            {\n" +
                "                \"name\": \"lop\",\n" +
                "                \"lang\": \"java\",\n" +
                "                \"_id\": \"3\",\n" +
                "                \"_type\": \"vertex\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"vadas\",\n" +
                "                \"age\": 27,\n" +
                "                \"_id\": \"2\",\n" +
                "                \"_type\": \"vertex\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"marko\",\n" +
                "                \"age\": 29,\n" +
                "                \"_id\": \"1\",\n" +
                "                \"_type\": \"vertex\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"peter\",\n" +
                "                \"age\": 35,\n" +
                "                \"_id\": \"6\",\n" +
                "                \"_type\": \"vertex\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"ripple\",\n" +
                "                \"lang\": \"java\",\n" +
                "                \"_id\": \"5\",\n" +
                "                \"_type\": \"vertex\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"josh\",\n" +
                "                \"age\": 32,\n" +
                "                \"_id\": \"4\",\n" +
                "                \"_type\": \"vertex\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"edges\": [\n" +
                "            {\n" +
                "                \"weight\": 1,\n" +
                "                \"_id\": \"10\",\n" +
                "                \"_type\": \"edge\",\n" +
                "                \"_outV\": \"4\",\n" +
                "                \"_inV\": \"5\",\n" +
                "                \"_label\": \"created\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"weight\": 0.5,\n" +
                "                \"_id\": \"7\",\n" +
                "                \"_type\": \"edge\",\n" +
                "                \"_outV\": \"1\",\n" +
                "                \"_inV\": \"2\",\n" +
                "                \"_label\": \"knows\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"weight\": 0.4000000059604645,\n" +
                "                \"_id\": \"9\",\n" +
                "                \"_type\": \"edge\",\n" +
                "                \"_outV\": \"1\",\n" +
                "                \"_inV\": \"3\",\n" +
                "                \"_label\": \"created\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"weight\": 1,\n" +
                "                \"_id\": \"8\",\n" +
                "                \"_type\": \"edge\",\n" +
                "                \"_outV\": \"1\",\n" +
                "                \"_inV\": \"4\",\n" +
                "                \"_label\": \"knows\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"weight\": 0.4000000059604645,\n" +
                "                \"_id\": \"11\",\n" +
                "                \"_type\": \"edge\",\n" +
                "                \"_outV\": \"4\",\n" +
                "                \"_inV\": \"3\",\n" +
                "                \"_label\": \"created\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"weight\": 0.20000000298023224,\n" +
                "                \"_id\": \"12\",\n" +
                "                \"_type\": \"edge\",\n" +
                "                \"_outV\": \"6\",\n" +
                "                \"_inV\": \"3\",\n" +
                "                \"_label\": \"created\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";
        System.out.println(TimeReport.time(10000, () -> Grammar.OBJECT.parse(json).value()));
    }
}
