package com.googlecode.totallylazy.transducers;

import java.util.List;

import static com.googlecode.totallylazy.Lists.list;
import static com.googlecode.totallylazy.Unchecked.cast;

public interface CompositeTransducer<A, B, C> extends Transducer<A, C> {
    Transducer<A, B> a();
    Transducer<B, C> b();

    static <A, B, C> Transducer<A, C> compositeTransducer(Transducer<A, B> a, Transducer<B, C> b) {
        if(a instanceof IdentityTransducer) return cast(b);
        if(b instanceof IdentityTransducer) return cast(a);
        return new CompositeTransducer<A,B,C>(){
            @Override public Transducer<A, B> a() { return a; }
            @Override public Transducer<B, C> b() { return b; }
        };
    }

    @Override
    default Receiver<A> apply(Receiver<C> receiver) {return a().apply(b().apply(receiver));}

    default List<Transducer<?, ?>> transducers() {
        return transducers(this);
    }

    static List<Transducer<?, ?>> transducers(Transducer<?, ?> transducer) {
        if(transducer instanceof CompositeTransducer) {
            CompositeTransducer compositeTransducer = (CompositeTransducer) transducer;
            List<Transducer<?, ?>> transducers = transducers(compositeTransducer.a());
            transducers.addAll(transducers(compositeTransducer.b()));
            return transducers;
        }
        return list(transducer);
    }


}
