package com.googlecode.totallylazy;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintStream;

import static java.lang.management.ManagementFactory.getRuntimeMXBean;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assume.assumeThat;

public class DebugTest {

    private final StringPrintStream stdErr = new StringPrintStream();

    private PrintStream originalStdErr;

    @Before
    public void redirectSystemErr() {
        originalStdErr = System.err;
        System.setErr(stdErr);
    }

    @After
    public void resetSystemErr() {
        System.setErr(originalStdErr);
    }

    @Test
    public void exceptionsAreNotPrintedToSystemErrorWhenJvmDebuggingIsDisabled() throws Exception {
        assumeThat(jvmArguments(), not(containsDebuggingAgent()));

        Debug.trace(new RuntimeException("aTestException"));

        assertThat(stdErr.toString(), not(containsString("aTestException")));
    }

    @Test
    public void exceptionsArePrintedToSystemErrorWhenJvmDebuggingIsEnabled() throws Exception {
        assumeThat(jvmArguments(), containsDebuggingAgent());

        Debug.trace(new RuntimeException("aTestException"));

        assertThat(stdErr.toString(), containsString("aTestException"));
    }

    @Test
    public void exceptionsAreNotPrintedToSystemErrorWhenJvmDebuggingIsEnabledAndSuppressionArgumentIsPresent() throws Exception {
        assumeThat(jvmArguments(), containsDebuggingAgent());
        System.setProperty("com.googlecode.totallylazy.trace", "false");

        Debug.trace(new RuntimeException("aTestException"));

        assertThat(stdErr.toString(), not(containsString("aTestException")));
    }

    private Matcher<String> containsDebuggingAgent() {
        return containsString("-agentlib:jdwp");
    }

    private String jvmArguments() {
        return getRuntimeMXBean().getInputArguments().toString();
    }

}