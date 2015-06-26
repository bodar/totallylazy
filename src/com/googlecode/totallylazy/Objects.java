package com.googlecode.totallylazy;

import com.googlecode.totallylazy.functions.Curried2;

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

    public static Curried2<Object, Object, Boolean> equalTo() {
        return Object::equals;
    }

}
