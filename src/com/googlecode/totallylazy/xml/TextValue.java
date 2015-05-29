package com.googlecode.totallylazy.xml;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.XMLEvent;

public class TextValue implements com.googlecode.totallylazy.Callable1<Location,String> {
    @Override
    public String call(Location location) throws Exception {
        StringBuilder result = new StringBuilder();
        XMLEventReader reader = location.reader();
        while (reader.hasNext()) {
            XMLEvent xmlEvent = reader.nextEvent();
            if(xmlEvent instanceof EndElement) return result.toString();
            if(xmlEvent instanceof Characters) {
                result.append(((Characters) xmlEvent).getData());
                continue;
            }
            throw new UnsupportedOperationException("Errrrr");
        }
        throw new UnsupportedOperationException("Errrrr");
    }
}
