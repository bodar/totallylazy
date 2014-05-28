package com.googlecode.totallylazy.numbers;

import static com.googlecode.totallylazy.PredicateAssert.assertThat;
import static com.googlecode.totallylazy.PredicateAssert.assertEquals;
import static com.googlecode.totallylazy.PredicateAssert.assertFalse;
import static com.googlecode.totallylazy.PredicateAssert.assertTrue;
import static com.googlecode.totallylazy.PredicateAssert.fail;

import java.math.BigInteger;

import org.junit.Test;

import com.googlecode.totallylazy.matchers.NumberMatcher;

public class ShortOperatorsTest {
    final short zero = 0;
    final short one = 1;
    final short two = 2;
    final short minusOne = -1;
    final short minusTwo = -2;
    final ShortOperators ops = ShortOperators.Instance;
    Number nr;
    Short sr;
    Ratio rr;

    @Test
    public void testNegateNormal() {
        nr = ops.negate(one);
        assertEquals(Short.class, nr.getClass());
        assertThat(nr, NumberMatcher.is(minusOne));
    }

    @Test
    public void testNegateZero() {
        nr = ops.negate(zero);
        assertEquals(Short.class, nr.getClass());
        assertThat(nr, NumberMatcher.is(0));
    }

    @Test
    public void testNegateMax() {
        nr = ops.negate(Short.MAX_VALUE);
        assertEquals(Short.class, nr.getClass());
        assertThat(nr, NumberMatcher.is((short) (-Short.MAX_VALUE)));
    }

    @Test
    public void testNegateMin() {
        nr = ops.negate(Short.MIN_VALUE);
        assertEquals(Integer.class, nr.getClass());
        assertThat(nr, NumberMatcher.is(-Short.MIN_VALUE));
    }

    @Test
    public void testIncrementNormal() {
        nr = ops.increment(one);
        assertEquals(Short.class, nr.getClass());
        assertThat(nr, NumberMatcher.is(two));
    }

    @Test
    public void testIncrementZero() {
        nr = ops.increment(zero);
        assertEquals(Short.class, nr.getClass());
        assertThat(nr, NumberMatcher.is(one));
    }

    @Test
    public void testIncrementMin() {
        nr = ops.increment(Short.MIN_VALUE);
        assertEquals(Short.class, nr.getClass());
        assertThat(nr, NumberMatcher.is(Short.MIN_VALUE + 1));
    }

    @Test
    public void testIncrementMax() {
        nr = ops.increment(Short.MAX_VALUE);
        assertEquals(Integer.class, nr.getClass());
        assertThat(nr, NumberMatcher.is(Short.MAX_VALUE + 1));
    }

    @Test
    public void testDecrementNormal() {
        nr = ops.decrement(one);
        assertEquals(Short.class, nr.getClass());
        assertThat(nr, NumberMatcher.is(zero));
    }

    @Test
    public void testDecrementZero() {
        nr = ops.decrement(zero);
        assertEquals(Short.class, nr.getClass());
        assertThat(nr, NumberMatcher.is(minusOne));
    }

    @Test
    public void testDecrementMin() {
        nr = ops.decrement(Short.MIN_VALUE);
        assertEquals(Integer.class, nr.getClass());
        assertThat(nr, NumberMatcher.is(Short.MIN_VALUE - 1));
    }

    @Test
    public void testDecrementMax() {
        nr = ops.decrement(Short.MAX_VALUE);
        assertEquals(Short.class, nr.getClass());
        assertThat(nr, NumberMatcher.is(Short.MAX_VALUE - 1));
    }

    @Test
    public void testIsZero() {
        assertTrue(ops.isZero(zero));
        assertFalse(ops.isZero(one));
        assertFalse(ops.isZero(Short.MAX_VALUE));
    }

    @Test
    public void testIsPositive() {
        assertTrue(ops.isPositive(one));
        assertFalse(ops.isPositive(zero));
        assertFalse(ops.isPositive(minusOne));
        assertTrue(ops.isPositive(Short.MAX_VALUE));
        assertFalse(ops.isPositive(Short.MIN_VALUE));
    }

    @Test
    public void testIsNegative() {
        assertFalse(ops.isNegative(one));
        assertFalse(ops.isNegative(zero));
        assertTrue(ops.isNegative(minusOne));
        assertFalse(ops.isNegative(Short.MAX_VALUE));
        assertTrue(ops.isNegative(Short.MIN_VALUE));
    }

    @Test
    public void testEqualTo() {
        assertTrue(ops.equalTo(one, (short) 1));
        assertTrue(ops.equalTo(two, (short) 2));
        assertFalse(ops.equalTo(zero, (short) 1));
    }

    @Test
    public void testLessThan() {
        assertTrue(ops.lessThan(zero, one));
        assertTrue(ops.lessThan(one, two));
        assertFalse(ops.lessThan(two, one));
        assertFalse(ops.lessThan(zero, zero));
        assertTrue(ops.lessThan(Short.MIN_VALUE, zero));
        assertFalse(ops.lessThan(Short.MAX_VALUE, zero));
    }

    @Test
    public void testAddNormal() {
        nr = ops.add(one, one);
        assertEquals(Short.class, nr.getClass());
        assertEquals(two, nr);
    }

    @Test
    public void testAddZero() {
        nr = ops.add(zero, one);
        assertEquals(Short.class, nr.getClass());
        assertEquals(one, nr);
    }

    @Test
    public void testAddOverflowMax() {
        nr = ops.add(Short.MAX_VALUE, Short.MAX_VALUE);
        assertEquals(Integer.class, nr.getClass());
        assertEquals(Short.MAX_VALUE + Short.MAX_VALUE, nr);
    }

    @Test
    public void testAddOverflowMin() {
        nr = ops.add(Short.MIN_VALUE, Short.MIN_VALUE);
        assertEquals(Integer.class, nr.getClass());
        assertEquals(Short.MIN_VALUE + Short.MIN_VALUE, nr);
    }

    @Test
    public void testMultiplyNormal() {
        nr = ops.multiply(one, two);
        assertEquals(Short.class, nr.getClass());
        assertEquals(two, nr);
    }

    @Test
    public void testMultiplyZero() {
        nr = ops.multiply(zero, one);
        assertEquals(Short.class, nr.getClass());
        assertEquals(zero, nr);
    }

    @Test
    public void testMultiplyOverflow0() {
        nr = ops.multiply(Short.MAX_VALUE, Short.MAX_VALUE);
        assertEquals(Integer.class, nr.getClass());
        assertEquals(Short.MAX_VALUE * Short.MAX_VALUE, nr);
    }

    @Test
    public void testMultiplyOverflow1() {
        nr = ops.multiply(Short.MAX_VALUE, Short.MIN_VALUE);
        assertEquals(Integer.class, nr.getClass());
        assertEquals(Short.MAX_VALUE * Short.MIN_VALUE, nr);
    }

    @Test
    public void testMultiplyOverflow2() {
        nr = ops.multiply(Short.MIN_VALUE, Short.MIN_VALUE);
        assertEquals(Integer.class, nr.getClass());
        assertEquals(Short.MIN_VALUE * Short.MIN_VALUE, nr);
    }

    @Test
    public void testDivide() {
        nr = ops.divide(one, two);
        final Ratio expected = new Ratio(BigInteger.valueOf(one),
                BigInteger.valueOf(two));
        assertEquals(Ratio.class, nr.getClass());
        assertEquals(expected, nr);
    }

    @Test
    public void testQuotientNormal() {
        nr = ops.quotient(one, two);
        assertEquals(Short.class, nr.getClass());
        assertEquals(zero, nr);
    }

    @Test(expected = ArithmeticException.class)
    public void testQuotientDivideByZero() {
        ops.quotient(one, zero);
        fail("Should throw ArithmeticException.");
    }

    @Test
    public void testRemainderNormal() {
        nr = ops.remainder(one, two);
        assertEquals(Short.class, nr.getClass());
        assertEquals(one, nr);
    }

    @Test(expected = ArithmeticException.class)
    public void testRemainderDivideByZero() {
        ops.remainder(one, zero);
        fail("Should throw ArithmeticException.");
    }

    @Test
    public void testForClass() {
        assertEquals(Short.class, ops.forClass());
    }

    @Test
    public void testPriority() {
        assertThat(ops.priority(),
                Numbers.lessThan(IntegerOperators.Instance.priority()));
    }
}
