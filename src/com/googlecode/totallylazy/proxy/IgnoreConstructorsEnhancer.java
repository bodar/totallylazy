package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Callable1;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.List;

import static com.googlecode.totallylazy.Callables.toClass;
import static com.googlecode.totallylazy.Sequences.sequence;

class IgnoreConstructorsEnhancer extends Enhancer {
    Objenesis objenesis = new ObjenesisStd();
    private Callback[] callbacks;

    @Override
    protected void filterConstructors(Class sc, List constructors) {
    }

    @Override
    public void setCallbacks(Callback[] callbacks) {
        this.callbacks = callbacks;
        setCallbackTypes(sequence(callbacks).map(toClass()).toArray(Class.class));
    }

    @Override
    public void setCallback(Callback callback) {
        setCallbacks(new Callback[]{callback});
    }

    @Override
    public Object create() {
        setUseFactory(true);
        Class proxyClass = createClass();
        Object instance = objenesis.newInstance(proxyClass);
        wireUpHandler(instance);
        return instance;

    }

    private void wireUpHandler(Object instance) {
        Factory factory = (Factory) instance;
        factory.setCallbacks(callbacks);
    }


}
