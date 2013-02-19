package com.googlecode.totallylazy;

import com.googlecode.totallylazy.matchers.NumberMatcher;
import com.googlecode.totallylazy.multi;
import org.junit.Test;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.multi.distanceBetween;
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

    public static class InterfaceOverSuperClass {
        public static String process(Object o) { return new multi(){}.method(o); }
        private static String process(Map s) { return "Map processed"; }
        private static String process(AbstractMap s) { return "AbstractMap processed"; }
    }

    @Test
    public void prefersInterfacesOverSuperClass() throws Exception {
        assertThat(distanceBetween(HashMap.class, Map.class), NumberMatcher.is(1));
        assertThat(distanceBetween(HashMap.class, AbstractMap.class), NumberMatcher.is(1.1));
        assertThat(InterfaceOverSuperClass.process((Object) new HashMap<String, String>()), is("Map processed"));
    }

    @Test
    public void distanceBetweenTwoClass() throws Exception {
        assertThat(distanceBetween(String.class, CharSequence.class), NumberMatcher.is(1));
        assertThat(distanceBetween(Integer.class, Serializable.class), NumberMatcher.is(2));
    }

    class Instance {
        public String process(Object o) { return new multi(){}.method(o); }
        private String process(String s) { return "String processed"; }
        private String process(CharSequence s) { return "CharSequence processed"; }
        private String process(Integer s) { return "Integer processed"; }
    }

    @Test
    public void worksWithInstances() throws Exception {
        assertThat(new Instance().process((Object) "A String"), is("String processed"));
    }

    @Test
    public void worksWithInstancesAndSuperClasses() throws Exception {
        class ExtendsInstance extends Instance{}
        assertThat(new ExtendsInstance().process((Object) "A String"), is("String processed"));
    }

    @Test
    public void supportsNotMatching() throws Exception {
        class Instance {
            public String process(Object o) { return new multi(){}.<String>methodOption(o).getOrElse("No match found"); }
        }
        assertThat(new Instance().process(10.f), is("No match found"));
    }

    @Test
    public void doesNotCallMethodsWithMoreArguments() throws Exception {
        class Instance {
            public String process(Object o) { return new multi(){}.<String>methodOption(o).getOrElse("No match found"); }
            public String process(Float a, Float b) {return "Float";}
        }
        assertThat(new Instance().process(10.f), is("No match found"));
    }

    @Test
    public void doesNotCallMethodsWithLessArguments() throws Exception {
        class Instance {
            public String process(Object o) { return new multi(){}.<String>methodOption(o).getOrElse("No match found"); }
            public String process() {return "no args";}
        }
        assertThat(new Instance().process(10.f), is("No match found"));
    }

    @Test
    public void doesNotRecurse() throws Exception {
        class Instance extends Eq {
        }
        assertThat(new Instance().equals(1), is(false));
    }

    @Test
    public void canMatchNullToAVoidMethod() throws Exception {
        class Instance extends Eq {
            public String process(Object o) { return new multi(){}.<String>methodOption(o).getOrElse("No match found"); }
            public String process(Float a) {return "Float";}
            public String process(Void a) {return "Void";}
        }
        assertThat(new Instance().process((Object) null), is("Void"));
    }

    @Test
    public void handlesNullWhenNoExplicitMatch() throws Exception {
        class Instance extends Eq {
            public String process(Object o) { return new multi(){}.<String>methodOption(o).getOrElse("No match found"); }
            public String process(Float a) {return "Float";}
        }
        assertThat(new Instance().process((Object)null), is("No match found"));
    }
}
