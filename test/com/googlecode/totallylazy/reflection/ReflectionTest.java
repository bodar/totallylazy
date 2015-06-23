package com.googlecode.totallylazy.reflection;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.reflection.Reflection.enclosingInstance;
import static com.googlecode.totallylazy.reflection.Fields.syntheticFields;
import static java.lang.String.format;

public class ReflectionTest {
    @Test
    public void testEnclosingInstance() throws Exception {
        Object myInnerClass = new MyInnerClass();
        Object outerClass = enclosingInstance(myInnerClass);
        assertThat(format("Did not find enclosing instance in fields: %s", syntheticFields(myInnerClass.getClass()).map(Fields.name) ), outerClass, is((Object)this));
    }

    private class MyInnerClass {
        final Object this$0 = "This might fool Reflection";
    }

    @Test
    public void supportsEnclosingMethod() throws Exception {
        Method actual = Reflection.enclosingMethod();
        Method expected = new Enclosing() {}.method();
        assertThat(actual, is(expected));
    }

    @Test
    public void supportsEnclosingConstructor() throws Exception {
        class Foo {
            Foo() {
                Constructor<?> actual = Reflection.enclosingConstructor();
                Constructor<?> expected = new Enclosing() {}.constructor();
                assertThat(actual, is(expected));
            }
        }

        new Foo();
    }

    @Test
    public void supportsCallingMethod() throws Exception {
        class Bar {
            Method instanceMethod() {
                return Reflection.callingMethod();
            }
        }

        Method actual = new Bar().instanceMethod();
        Method expected = new Enclosing() {}.method();
        assertThat(actual, is(expected));
    }

    static class Baz {
        static Method staticMethod() {
            return Reflection.callingMethod();
        }
    }

    @Test
    public void supportsStaticCallingMethod() throws Exception {
        Method actual = Baz.staticMethod();
        Method expected = new Enclosing() {}.method();
        assertThat(actual, is(expected));
    }

    @Test
    public void supportsCallingClass() throws Exception {
        class Bar {
            Class<?> who() {
                return Reflection.callingClass();
            }
        }

        Class<?> actual = new Bar().who();
        assertThat(actual, is(getClass()));
    }

}
