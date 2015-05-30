package com.googlecode.totallylazy.xml;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.XMLEvent;

public class StreamingXml {
    public static String text(Location location) throws XMLStreamException {
        XMLEventReader reader = location.reader();
        StringBuilder result = new StringBuilder();
        while (reader.hasNext()) {
            XMLEvent xmlEvent = reader.nextEvent();
            if(xmlEvent instanceof EndElement) return result.toString();
            if(xmlEvent instanceof Characters) {
                result.append(((Characters) xmlEvent).getData());
                continue;
            }
            throw new UnsupportedOperationException("TODO: Current don't support child elements");
        }
        return result.toString();
    }

    public static String currentName(Location field) {
        return field.current().getName().getLocalPart();
    }

}
