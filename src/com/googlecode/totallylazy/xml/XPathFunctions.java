package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.Xml;
import com.googlecode.totallylazy.regex.Regex;
import com.googlecode.totallylazy.time.Dates;
import com.googlecode.totallylazy.xml.FunctionResolver;
import com.googlecode.totallylazy.xml.NodeArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.xml.xpath.XPathFunctionResolver;
import java.util.List;

import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.nullValue;
import static com.googlecode.totallylazy.Sequences.sequence;

public class XPathFunctions {
    @XPathFunction("trim-and-join")
    public static String trimAndJoin(NodeList nodes, String delimiter) {
        return Xml.textContents(nodes).map(Strings.trim()).toString(unescape(delimiter));
    }

    @XPathFunction("string-join")
    public static String stringJoin(NodeList nodes, String delimiter) {
        return Xml.textContents(nodes).toString(unescape(delimiter));
    }

    @XPathFunction("if")
    public static Object IF(NodeList nodeList, Object matched, Object notMatched) {
        return nodeList.getLength() > 0 ? matched : notMatched;
    }

    @XPathFunction("or")
    public static Object or(List<Object> arguments) {
        return sequence(arguments).find(not(nullValue()).and(new Predicate<Object>() {
            @Override
            public boolean matches(Object other) {
                return !(other instanceof NodeList) || ((NodeList) other).getLength() != 0;
            }
        })).getOrNull();
    }

    @XPathFunction("tokenize")
    public static NodeArrayList<Text> tokenize(NodeList input, String pattern) {
        return new NodeArrayList<Text>(Xml.sequence(input).flatMap(split(pattern)));
    }

    @XPathFunction("time-in-millis")
    public static Long timeInMillis(NodeList dates) {
        Option<String> date = Xml.textContents(dates).headOption();
        return date.map(new Function1<String, Long>() {
            @Override
            public Long call(String s) throws Exception {
                return Dates.parse(s).getTime();
            }
        }).getOrNull();
    }

    @XPathFunction("date-in-millis")
    public static Long dateInMillis(NodeList dates) {
        Option<String> date = Xml.textContents(dates).headOption();
        return date.map(new Function1<String, Long>() {
            @Override
            public Long call(String s) throws Exception {
                return Dates.stripTime(Dates.parse(s)).getTime();
            }
        }).getOrNull();
    }

    private static Function1<Node, Sequence<Text>> split(final String pattern) {
        return new Function1<Node, Sequence<Text>>() {
            @Override
            public Sequence<Text> call(final Node node) throws Exception {
                return Regex.regex(pattern).split(node.getTextContent()).map(createText(node));                    }
        };
    }

    private static Function1<String, Text> createText(final Node node) {
        return new Function1<String, Text>() {
            @Override
            public Text call(String text) throws Exception {
                return node.getOwnerDocument().createTextNode(text);
            }
        };
    }

    private static String unescape(String value) {
        return value.replace("\\n", "\n");
    }


}