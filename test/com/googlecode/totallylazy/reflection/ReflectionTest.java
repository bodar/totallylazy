package com.googlecode.totallylazy.reflection;

import com.googlecode.totallylazy.Characters;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.reflection.Reflection.enclosingInstance;
import static com.googlecode.totallylazy.reflection.Fields.syntheticFields;
import static java.lang.String.format;

public class ReflectionTest {
    @Test
    public void canBoxPrimitiveClasses() throws Exception {
        assertThat(Reflection.box(void.class), is(Void.class));
        assertThat(Reflection.box(char.class), is(Character.class));
        assertThat(Reflection.box(boolean.class), is(Boolean.class));
        assertThat(Reflection.box(byte.class), is(Byte.class));
        assertThat(Reflection.box(short.class), is(Short.class));
        assertThat(Reflection.box(int.class), is(Integer.class));
        assertThat(Reflection.box(long.class), is(Long.class));
        assertThat(Reflection.box(float.class), is(Float.class));
        assertThat(Reflection.box(double.class), is(Double.class));
    }

    @Test
    public void canUnBoxPrimitiveClasses() throws Exception {
        assertThat(Reflection.unbox(Void.class), is(void.class));
        assertThat(Reflection.unbox(Character.class), is(char.class));
        assertThat(Reflection.unbox(Boolean.class), is(boolean.class));
        assertThat(Reflection.unbox(Byte.class), is(byte.class));
        assertThat(Reflection.unbox(Short.class), is(short.class));
        assertThat(Reflection.unbox(Integer.class), is(int.class));
        assertThat(Reflection.unbox(Long.class), is(long.class));
        assertThat(Reflection.unbox(Float.class), is(float.class));
        assertThat(Reflection.unbox(Double.class), is(double.class));
    }

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
