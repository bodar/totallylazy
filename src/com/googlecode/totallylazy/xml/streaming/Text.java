package com.googlecode.totallylazy.xml.streaming;

import javax.xml.stream.events.Characters;

public class Text implements Node {
    private final String value;

    private Text(String value) {this.value = value;}

    public static Text text(Characters value){
        return text(value.getData());
    }

    public static Text text(String value){
        return new Text(value);
    }

    @Override
    public String text() { return value; }

    @Override
    public boolean isText() { return true; }

    @Override
    public String toString() {
        return text();
    }
}
