package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.matchers.NumberMatcher;
import org.junit.Ignore;
import org.junit.Test;

import java.io.Serializable;

import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.matchers.Matchers.matcher;
import static com.googlecode.totallylazy.proxy.multi.distanceBetween;
import static org.hamcrest.MatcherAssert.assertThat;

public class multiTest {
    public static class StaticSingle {
        public static String process(Object o) { return new multi() {}.method(o); }
        private static String process(String s) { return "String processed"; }
    }

    @Test
    public void canDispatchWithSingle() throws Exception {
        assertThat(StaticSingle.process((Object) "A String"), is("String processed"));
    }

    public static class Static2 {
        public static String process(Object o) { return new multi() {}.method(o); }
        private static String process(String s) { return "String processed"; }
        private static String process(Integer s) { return "Integer processed"; }
    }

    @Test
    public void canDispatchMethodBasedOnType() throws Exception {
        assertThat(Static2.process((Object) "A String"), is("String processed"));
    }

    public static class Static3 {
        public static String process(Object o) { return new multi(){}.method(o); }
        private static String process(CharSequence s) { return "CharSequence processed"; }
        private static String process(Integer s) { return "Integer processed"; }
    }

    @Test
    public void willUseSuperTypesIfNoExactMatchFound() throws Exception {
        assertThat(Static3.process((Object) "A String"), is("CharSequence processed"));
    }

    public static class Static4 {
        public static String process(Object o) { return new multi(){}.method(o); }
        private static String process(String s) { return "String processed"; }
        private static String process(CharSequence s) { return "CharSequence processed"; }
        private static String process(Integer s) { return "Integer processed"; }
    }

    @Test
    public void willCallMostSpecific() throws Exception {
        assertThat(Static4.process((Object) "A String"), is("String processed"));
    }

    @Test
    public void distanceBetweenTwoClass() throws Exception {
        assertThat(distanceBetween(String.class, CharSequence.class), NumberMatcher.is(1));
        assertThat(distanceBetween(Integer.class, Serializable.class), NumberMatcher.is(2));

    }

}
