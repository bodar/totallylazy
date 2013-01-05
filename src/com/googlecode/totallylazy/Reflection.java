package com.googlecode.totallylazy;

public class Reflection {
    public static Object enclosingInstance(Object innerClass) {
        try {
            return innerClass.getClass().getDeclaredField("this$0").get(innerClass);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
