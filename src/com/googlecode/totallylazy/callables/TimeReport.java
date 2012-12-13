package com.googlecode.totallylazy.callables;


import com.googlecode.totallylazy.Block;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Runnables;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.numbers.Numbers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.numbers.Numbers.add;
import static com.googlecode.totallylazy.numbers.Numbers.ascending;
import static com.googlecode.totallylazy.numbers.Numbers.descending;

public class TimeReport extends Block<Number> {
    private final List<Number> times = new ArrayList<Number>();

    @Override
    protected void execute(Number time) throws Exception {
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
        return String.format("Elapsed msecs for %s runs:\tAvg:%.5f\tMin:%.5f\tMax:%.5f\tTotal:%.5f", runs(), average(), minimum(), maximum(), total());
    }

    public double minimum() {
        return sequence(times).sortBy(ascending()).head().doubleValue();
    }

    public double maximum() {
        return sequence(times).sortBy(descending()).head().doubleValue();
    }

    public double average() {
        return sequence(times).
                sortBy(ascending()).
                drop(tenPercent()).
                reverse().
                drop(tenPercent()).
                reduce(Numbers.average()).
                doubleValue();
    }

    private int tenPercent() {
        return (int) Math.floor(times.size() * 0.10);
    }

    private int runs() {
        return times.size();
    }

    public double total() {
        return sequence(times).reduce(add()).doubleValue();
    }

    public static TimeReport time(int numberOfCalls, Sequence<?> sequence) {
        TimeReport report = new TimeReport();
        repeat(TimeCallable.time(sequence, report)).take(numberOfCalls).realise();
        return report;
    }

    public static TimeReport time(int numberOfCalls, Callable<?> callable) {
        TimeReport report = new TimeReport();
        repeat(TimeCallable.time(callable, report)).take(numberOfCalls).realise();
        return report;
    }
}
