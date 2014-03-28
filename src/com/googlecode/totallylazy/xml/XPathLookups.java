package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Mapper;
import com.googlecode.totallylazy.Xml;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class XPathLookups {
    private static final Map<String, Function1<String, String>> lookups = new ConcurrentHashMap<String, Function1<String, String>>();

    @XPathFunction("lookup")
    public static NodeArrayList lookup(String name, NodeList types) {
        return new NodeArrayList<Text>(Xml.sequence(types).map(lookup(lookups.get(name))));
    }

    private static Mapper<Node, Text> lookup(final Function1<String, String> data) {
        return new Mapper<Node, Text>() {
            @Override
            public Text call(Node node) throws Exception {
                return XPathFunctions.createText(node, data.call(node.getTextContent()));
            }
        };
    }

    public static void setLookup(String name, Function1<String, String> lookup){
        lookups.put(name, lookup);
    }

}
