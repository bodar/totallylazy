package com.googlecode.totallylazy.json;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.callables.TimeReport;
import com.googlecode.totallylazy.matchers.NumberMatcher;
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
    public void foo() throws Exception {
        final String json = "{\"edges\":[{\"_id\":\"509562\",\"_inV\":\"365673\",\"_label\":\"IdentifierFor\",\"_outV\":\"509561\",\"_type\":\"edge\"},{\"_id\":\"846227\",\"_inV\":\"365673\",\"_label\":\"Has Counterparty\",\"_outV\":\"222733\",\"_type\":\"edge\"},{\"_id\":\"846218\",\"_inV\":\"355834\",\"_label\":\"Has Counterparty\",\"_outV\":\"222733\",\"_type\":\"edge\"},{\"_id\":\"694571\",\"_inV\":\"355834\",\"_label\":\"Has Principal\",\"_outV\":\"222733\",\"_type\":\"edge\"},{\"_id\":\"0\",\"_inV\":\"222733\",\"_label\":\"Has Principal\",\"_outV\":\"355834\",\"_type\":\"edge\"},{\"_id\":\"1\",\"_inV\":\"222733\",\"_label\":\"Has Principal\",\"_outV\":\"355834\",\"_type\":\"edge\"},{\"_id\":\"2\",\"_inV\":\"222733\",\"_label\":\"Has Principal\",\"_outV\":\"355834\",\"_type\":\"edge\"},{\"_id\":\"694570\",\"_inV\":\"11\",\"_label\":\"Has Reciprocity\",\"_outV\":\"222733\",\"_type\":\"edge\"},{\"_id\":\"280987\",\"_inV\":\"280985\",\"_label\":\"Has Collateral\",\"_outV\":\"222733\",\"_type\":\"edge\"},{\"_id\":\"222737\",\"_inV\":\"222734\",\"_label\":\"Has Collateral\",\"_outV\":\"222733\",\"_type\":\"edge\"},{\"_id\":\"222735\",\"_inV\":\"5\",\"_label\":\"Has Asset Class\",\"_outV\":\"222734\",\"_type\":\"edge\"},{\"_id\":\"222736\",\"_inV\":\"4\",\"_label\":\"Has Currency\",\"_outV\":\"222734\",\"_type\":\"edge\"},{\"_id\":\"509561--61705216:Request For\",\"_inV\":\"509561\",\"_label\":\"Request For\",\"_outV\":-61705216,\"_type\":\"edge\"},{\"_id\":\"355834--61705216:Request For\",\"_inV\":\"355834\",\"_label\":\"Request For\",\"_outV\":-61705216,\"_type\":\"edge\"},{\"_id\":\"1822626128-11:Funding Name for\",\"_inV\":1822626128,\"_label\":\"Funding Name for\",\"_outV\":\"11\",\"_type\":\"edge\"},{\"_id\":\"1822626128-4:Funding Name for\",\"_inV\":1822626128,\"_label\":\"Funding Name for\",\"_outV\":\"4\",\"_type\":\"edge\"},{\"_id\":\"1822626128-5:Funding Name for\",\"_inV\":1822626128,\"_label\":\"Funding Name for\",\"_outV\":\"5\",\"_type\":\"edge\"}],\"vertices\":[{\"Counterparty\":\"JPM199480NY\",\"Type\":\"Alias\",\"_id\":\"509561\",\"_type\":\"vertex\"},{\"Description\":\"Commingled Pension Trust Fund (Mortgage Private Placement) of JP Morgan Chase Bank\",\"Org Id\":\"6928316\",\"Type\":\"Counterparty\",\"_id\":\"365673\",\"_type\":\"vertex\"},{\"Description\":\"DEUTSCHE BANK AG\",\"Org Id\":\"157\",\"Type\":\"Counterparty\",\"_id\":\"355834\",\"_type\":\"vertex\"},{\"CSA Id\":\"52065\",\"Live Status\":\"Live\",\"Type\":\"Agreement\",\"_id\":\"222733\",\"_type\":\"vertex\"},{\"Description\":\"Bilateral\",\"Funding Name\":\"csa-2way\",\"Type\":\"Reciprocity\",\"_id\":\"11\",\"_type\":\"vertex\"},{\"Description\":\"JPM (199480) C52065\",\"Hash\":472548400,\"Type\":\"Asset\",\"_id\":\"280985\",\"_type\":\"vertex\"},{\"Description\":\"JPM (199480) C52065\",\"Hash\":-300971632,\"Type\":\"Asset\",\"_id\":\"222734\",\"_type\":\"vertex\"},{\"Cheapest To Deliver\":5,\"Description\":\"US Dollars\",\"Funding Name\":\"usd\",\"ISO Currency Code\":\"USD\",\"Type\":\"Currency\",\"_id\":\"4\",\"_type\":\"vertex\"},{\"Description\":\"Cash\",\"Funding Name\":\"cash\",\"Preference\":1,\"Type\":\"Asset Class\",\"_id\":\"5\",\"_type\":\"vertex\"},{\"Type\":\"Request\",\"_id\":-61705216,\"_type\":\"vertex\",\"counterparty\":\"JPM199480NY\",\"internalEntity\":\"157\"},{\"Funding Name\":\"csa-2way-usd-cash\",\"Type\":\"Funding Name\",\"_id\":1822626128,\"_type\":\"vertex\"}]}";
        System.out.println(TimeReport.time(1000, new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Map<String, Object> value = Grammar.OBJECT.parse(json).value();
                return null;
            }
        }));
    }
}
