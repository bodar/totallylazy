package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Seq;
import com.googlecode.totallylazy.comparators.Maximum;
import com.googlecode.totallylazy.comparators.Minimum;

import static com.googlecode.totallylazy.Sequences.iterate;

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

    public static Seq<Integer> range(final int start) {
        return iterate(i -> i + 1, start);
    }

    public static Seq<Integer> range(final int start, final int end) {
        if (end < start) return range(start, end, -1);
        return range(start).takeWhile(i -> i <= end);
    }

    public static Seq<Integer> range(final int start, final int end, final int step) {
        if (end < start) return iterate(i -> i + step, start).takeWhile(i -> i >= end);
        return iterate(i -> i + step, start).takeWhile(i -> i <= end);
    }
}
