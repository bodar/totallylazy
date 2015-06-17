package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Curried2;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.Xml;
import com.googlecode.totallylazy.regex.Regex;
import com.googlecode.totallylazy.time.Dates;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

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

    @XPathFunction("replace")
    public static NodeArrayList<Text> tokenize(NodeList input, String pattern, String replace) {
        return new NodeArrayList<Text>(Xml.sequence(input).map(replace(pattern, replace)));
    }

    @XPathFunction("time-in-millis")
    public static Long timeInMillis(NodeList dates) {
        Option<String> date = Xml.textContents(dates).headOption();
        return date.map(new Function<String, Long>() {
            @Override
            public Long call(String s) throws Exception {
                return Dates.parse(s).getTime();
            }
        }).getOrNull();
    }

    @XPathFunction("date-in-millis")
    public static Long dateInMillis(NodeList dates) {
        Option<String> date = Xml.textContents(dates).headOption();
        return date.map(new Function<String, Long>() {
            @Override
            public Long call(String s) throws Exception {
                return Dates.stripTime(Dates.parse(s)).getTime();
            }
        }).getOrNull();
    }

    private static Function<Node, Sequence<Text>> split(final String pattern) {
        return new Function<Node, Sequence<Text>>() {
            @Override
            public Sequence<Text> call(final Node node) throws Exception {
                return Regex.regex(pattern).split(node.getTextContent()).map(createText.apply(node));                    }
        };
    }

    private static Function<Node, Text> replace(final String pattern, final String replace) {
        return new Function<Node, Text>() {
            @Override
            public Text call(final Node node) throws Exception {
                return createText(node, node.getTextContent().replaceAll(pattern, replace));                   }
        };
    }

    public static Curried2<Node, String, Text> createText = new Curried2<Node, String, Text>() {
        @Override
        public Text call(Node node, String s) throws Exception {
            return createText(node, s);
        }
    };

    public static Text createText(Node nodeInDocument, String text) {return nodeInDocument.getOwnerDocument().createTextNode(text);}

    private static String unescape(String value) {
        return value.replace("\\n", "\n");
    }

}