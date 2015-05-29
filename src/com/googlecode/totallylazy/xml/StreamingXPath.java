package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import javax.xml.namespace.QName;
import javax.xml.stream.events.StartElement;

import static com.googlecode.totallylazy.predicates.LogicalPredicate.logicalPredicate;

public class StreamingXPath {
    public static LogicalPredicate<StartElement> name(String value){
        return logicalPredicate((Predicate<StartElement>) element -> element.getName().getLocalPart().equals(value));
    }

    public static LogicalPredicate<StartElement> any(){
        return Predicates.any();
    }

    public static LogicalPredicate<StartElement> attribute(String name, Predicate<String> value){
        return logicalPredicate((Predicate<StartElement>) element -> value.matches(element.getAttributeByName(new QName(name)).getValue()));
    }

    public static Predicate<Location> descendant(Predicate<? super StartElement> predicate) {
        return path -> predicate.matches(path.current());
    }
}
