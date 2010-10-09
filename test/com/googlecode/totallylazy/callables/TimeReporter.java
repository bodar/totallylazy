package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Runnable1;

public class TimeReporter implements Runnable1<Double> {
    private double time = 0;

    public void run(Double time) {
        this.time = time;
    }

    public double time() {
        return time;
    }
}
