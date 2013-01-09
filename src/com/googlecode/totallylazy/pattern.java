package com.googlecode.totallylazy;

public abstract class pattern {
    private final Object[] argument;

    public pattern(Object... argument) {
        this.argument = argument;
    }

    public <T> T match() { return new multi(){}.method(argument); }
}
