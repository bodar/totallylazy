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

    public static Function2<Object, Object, Boolean> equalTo() {
        return new Function2<Object, Object, Boolean>() {
            @Override
            public Boolean call(Object a, Object b) throws Exception {
                return a.equals(b);
            }
        };
    }

    public static class parameters {
        public static Object a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,_;
    }
}
