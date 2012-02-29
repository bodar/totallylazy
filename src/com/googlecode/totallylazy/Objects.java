package com.googlecode.totallylazy;

public class Objects {
    public static boolean equalTo(Object a, Object b) {
        if( a == null && b == null) {
            return true;
        }
        if( a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }
}
