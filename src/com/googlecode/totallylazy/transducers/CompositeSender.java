package com.googlecode.totallylazy.transducers;

import com.googlecode.totallylazy.annotations.multimethod;
import com.googlecode.totallylazy.multi;

import java.util.List;

public class CompositeSender<A, B> implements Sender<B> {
    private final Sender<A> sender;
    private final Transducer<A, B> transducer;

    private CompositeSender(Sender<A> sender, Transducer<A, B> transducer) {
        this.sender = sender;
        this.transducer = transducer;
    }

    @multimethod
    static <A, B, C> CompositeSender<A, C> compositeSender(CompositeSender<A, B> sender, Transducer<B, C> transducer) {
        return compositeSender(sender.sender, Transducers.compose(sender.transducer, transducer));
    }

    private static multi method;
    static <A, B> CompositeSender<A, B> compositeSender(Sender<A> sender, Transducer<A, B> transducer) {
        if(method == null) method = new multi() { };
        return method.<CompositeSender<A, B>>methodOption(sender, transducer).
                getOrElse(() -> new CompositeSender<>(sender, transducer));
    }

    @Override
    public AutoCloseable send(Receiver<B> receiver) {
        return sender.send(transducer.apply(receiver));
    }

    public Sender<A> sender() { return sender; }

    public Transducer<A, B> transducer() { return transducer; }

    public List<Transducer<?, ?>> transducers() {
        return CompositeTransducer.transducers(transducer);
    }
}
