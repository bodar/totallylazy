package com.googlecode.totallylazy.callables;


import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Runnables;
import com.googlecode.totallylazy.numbers.Numbers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.numbers.Numbers.*;

public class TimeReport implements Callable1<Double, Void> {
    private final List<Number> times = new ArrayList<Number>();

    public Void call(Double time) {
        this.times.add(time);
        return Runnables.VOID;
    }

    public double lastTime() {
        return times.get(times.size() - 1).doubleValue();
    }

    public void reset() {
        times.clear();
    }

    @Override
    public String toString() {
        return String.format("Elapsed msecs for %s runs:\tAvg:%s\tMin:%s\tMax:%s\tTotal:%s", runs(), average(), minimum(), maximum(), total());
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
                reduce(Numbers.average()).doubleValue();
    }

    private int tenPercent() {
        return (int) Math.floor(times.size() * 0.10);
    }

    private int runs() {
        return times.size();
    }

    public Number total() {
        return sequence(times).reduce(add());
    }

    public static TimeReport reportTime(Callable<?> callable, int numberOfCalls){
        TimeReport report = new TimeReport();
        repeat(TimeCallable.time(callable, report)).take(numberOfCalls).realise();
        return report;
    }
}
