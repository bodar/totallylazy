package com.googlecode.totallylazy.reflection;

import org.junit.Test;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.predicates.Predicates.is;

public class StackFramesTest {
    @Test
    public void doesNotReturnSelfInFrame() throws Exception {
        String methodName = StackFrames.stackFrames().head().trace().getMethodName();
        assertThat(methodName, is("doesNotReturnSelfInFrame"));
    }
}