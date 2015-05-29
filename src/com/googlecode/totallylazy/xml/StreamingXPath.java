package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;

import javax.xml.namespace.QName;
import javax.xml.stream.events.StartElement;

public class StreamingXPath {
    public static Predicate<StartElement> name(String value){
        return element -> element.getName().getLocalPart().equals(value);
    }

    public static Predicate<StartElement> any(){
        return Predicates.any();
    }

    public static Predicate<StartElement> attribute(String name, Predicate<String> value){
        return element -> value.matches(element.getAttributeByName(new QName(name)).getValue());
    }

    public static Predicate<Location> descendant(Predicate<? super StartElement> predicate) {
        return path -> predicate.matches(path.current());
    }


}
