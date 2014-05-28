package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Reflection.enclosingInstance;
import static com.googlecode.totallylazy.Fields.syntheticFields;
import static com.googlecode.totallylazy.Predicates.is;
import static java.lang.String.format;
import static com.googlecode.totallylazy.Assert.assertThat;

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
}
