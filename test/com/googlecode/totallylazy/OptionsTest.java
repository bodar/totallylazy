package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Option.*;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.junit.Assert.assertThat;

public class OptionsTest {

    @Test
    public void ignoresEmptySequences() throws Exception {
        assertThat(Options.flatten(sequence()), is(sequence()));
    }

    @Test
    public void removesUndefinedOptions() throws Exception {
        assertThat(Options.flatten(sequence(none(), none(), none())), is(sequence()));
    }

    @Test
    public void unpacksDefinedOptions() throws Exception {
        assertThat(Options.flatten(sequence(some("beyonce"), some("kelly"), some("michelle"))), is(sequence("beyonce", "kelly", "michelle")));
    }

    @Test
    public void lovesMixedSequences() throws Exception {
        assertThat(Options.flatten(sequence(some("beyonce"), none(), some("kelly"), none(), some("michelle"))), is(sequence("beyonce", "kelly", "michelle")));
    }
}