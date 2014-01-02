package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.comparators.Maximum;
import com.googlecode.totallylazy.comparators.Minimum;

public class Integers {
    public static Maximum.Function<Integer> maximum() {
        return Maximum.constructors.maximum(Integer.MIN_VALUE);
    }

    public static Minimum.Function<Integer> minimum() {
        return Minimum.constructors.minimum(Integer.MAX_VALUE);
    }

    public static class parameters {
        public static int a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,_;
    }
}
