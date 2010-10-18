package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Runnable1;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.reduceLeft;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.numbers.Numbers.*;
import static com.googlecode.totallylazy.numbers.Numbers.divide;

public class TimeReport implements Runnable1<Double> {
    private final List<Number> times = new ArrayList<Number>();

    public void run(Double time) {
        this.times.add(time);
    }

    public double lastTime() {
        return times.get(times.size() - 1).doubleValue();
    }

    public void reset() {
        times.clear();
    }

    @Override
    public String toString() {
        return String.format("Elapsed msecs for %s runs:\tAvg:%s\tMin:%s\tMax:%s", runs(), average(), minimum(), maximum());
    }

    public double minimum() {
        return sequence(times).sortBy(ascending()).head().doubleValue();
    }

    public double maximum() {
        return sequence(times).sortBy(descending()).head().doubleValue();
    }

    public double average() {
        List<Number> excludeTopAndBottom = sequence(times).sortBy(ascending()).drop(tenPercent()).reverse().drop(tenPercent()).toList();
        return divide(reduceLeft(excludeTopAndBottom, add()), excludeTopAndBottom.size()).doubleValue();
    }

    private int tenPercent() {
        return (int) Math.floor(times.size() * 0.10);
    }

    private int runs() {
        return times.size();
    }
}
