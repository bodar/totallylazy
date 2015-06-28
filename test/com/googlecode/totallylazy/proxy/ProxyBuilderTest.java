package com.googlecode.totallylazy.proxy;

import org.junit.Test;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.proxy.ProxyBuilder.proxy;

public class ProxyBuilderTest {

    public static abstract class BooleanArguments {
        public abstract boolean add(boolean a, boolean b);
    }

    @Test
    public void supportsBoolean() throws Throwable {
        BooleanArguments instance = proxy(BooleanArguments.class, (proxy, method, args) -> true);
        assertThat(instance.add(false, false), is(true));
    }

    public static abstract class IntegerArguments {
        public abstract int add(int a, int b);
    }

    @Test
    public void supportsInteger() throws Throwable {
        IntegerArguments instance = proxy(IntegerArguments.class, (proxy, method, args) -> 12);
        assertThat(instance.add(1, 2), is(12));
    }

    public static abstract class LongArguments {
        public abstract long add(long a, long b);
    }

    @Test
    public void supportsLong() throws Throwable {
        LongArguments instance = proxy(LongArguments.class, (proxy, method, args) -> 12L);
        assertThat(instance.add(1L, 2L), is(12L));
    }

    public static abstract class FloatArguments {
        public abstract float add(float a, float b);
    }

    @Test
    public void supportsFloat() throws Throwable {
        FloatArguments instance = proxy(FloatArguments.class, (proxy, method, args) -> 12F);
        assertThat(instance.add(1F, 2F), is(12F));
    }

    public static abstract class DoubleArguments {
        public abstract double add(double a, double b);
    }

    @Test
    public void supportsDouble() throws Throwable {
        DoubleArguments instance = proxy(DoubleArguments.class, (proxy, method, args) -> 12D);
        assertThat(instance.add(1D, 2D), is(12D));
    }

    public static abstract class ByteArguments {
        public abstract byte add(byte a, byte b);
    }

    @Test
    public void supportsByte() throws Throwable {
        ByteArguments instance = proxy(ByteArguments.class, (proxy, method, args) -> (byte)12);
        assertThat(instance.add((byte)1, (byte)2), is((byte)12));
    }

    public static abstract class ShortArguments {
        public abstract short add(short a, short b);
    }

    @Test
    public void supportsShort() throws Throwable {
        ShortArguments instance = proxy(ShortArguments.class, (proxy, method, args) -> (short)12);
        assertThat(instance.add((short)1, (short)2), is((short)12));
    }   
    
    public static abstract class CharArguments {
        public abstract char add(char a, char b);
    }

    @Test
    public void supportsChar() throws Throwable {
        CharArguments instance = proxy(CharArguments.class, (proxy, method, args) -> (char)12);
        assertThat(instance.add((char)1, (char)2), is((char)12));
    }
    
}