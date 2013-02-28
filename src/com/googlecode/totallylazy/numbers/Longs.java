package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.comparators.Maximum;
import com.googlecode.totallylazy.comparators.Minimum;

public class Longs {
    public static Maximum.Function<Long> maximum() {
        return Maximum.constructors.maximum(Long.MIN_VALUE);
    }

    public static Minimum.Function<Long> minimum() {
        return Minimum.constructors.minimum(Long.MAX_VALUE);
    }

    public static class parameters {
        public static long a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,_;
    }

}
