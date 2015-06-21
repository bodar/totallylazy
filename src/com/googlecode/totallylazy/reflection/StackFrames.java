package com.googlecode.totallylazy.reflection;

import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.Sequences.sequence;

public class StackFrames {
    public static Sequence<StackFrame> stackFrames() {
        return sequence(Thread.currentThread().getStackTrace()).tail().map(StackFrame::new);
    }
}
