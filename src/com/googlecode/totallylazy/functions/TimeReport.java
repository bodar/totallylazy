package com.googlecode.totallylazy.functions;


import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.numbers.Numbers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.numbers.Numbers.add;
import static com.googlecode.totallylazy.numbers.Numbers.ascending;
import static com.googlecode.totallylazy.numbers.Numbers.descending;
import static com.googlecode.totallylazy.numbers.Numbers.multiply;

public class TimeReport implements Block<Number> {
    private final List<Number> times = new ArrayList<Number>();

    @Override
    public void execute(Number time) throws Exception {
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

    public int runs() {
        return times.size();
    }

    public double total() {
        return sequence(times).reduce(add()).doubleValue();
    }

    public static TimeReport time(int numberOfCalls, Sequence<?> sequence) {
        TimeReport report = new TimeReport();
        repeat(TimeFunction0.time(sequence, report)).take(numberOfCalls).realise();
        return report;
    }

    public static TimeReport time(int numberOfCalls, Callable<?> callable) {
        TimeReport report = new TimeReport();
        repeat(TimeFunction0.time(callable, report)).take(numberOfCalls).realise();
        return report;
    }

    public static void timeRatio(final Callable<?> function) {
        iterate(multiply(2), 125).map(time(function)).reduce(new CurriedBinaryFunction<TimeReport>() {
            @Override
            public TimeReport call(TimeReport previous, TimeReport current) throws Exception {
                Number ratio = Numbers.divide(current.average(), previous.average());
                System.out.println("Ratio:" + ratio + " " + current);
                return current;
            }
        });
    }

    private static Function1<Number, TimeReport> time(final Callable<?> function) {
        return number -> time(number.intValue(), function);
    }
}
