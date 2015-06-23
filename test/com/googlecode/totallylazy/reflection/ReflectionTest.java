package com.googlecode.totallylazy.reflection;

import org.junit.Test;

import static com.googlecode.totallylazy.reflection.Reflection.enclosingInstance;
import static com.googlecode.totallylazy.reflection.Fields.syntheticFields;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;

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

    }
}
