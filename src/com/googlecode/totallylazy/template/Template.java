package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Mapper;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.annotations.multimethod;
import com.googlecode.totallylazy.multi;

import java.util.Map;

import static com.googlecode.totallylazy.Callables.toString;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Template implements Renderer<Map<String, Object>> {
    private final Sequence<Renderer<Map<String, Object>>> objects;

    public Template(Iterable<Renderer<Map<String, Object>>> objects) {
        this.objects = sequence(objects);
    }

    public String render(final Map<String, Object> map) throws Exception {
        return objects.map(functions.render(map)).toString(Strings.EMPTY);
    }

    @Override
    public String toString() {
        return objects.map(new Mapper<Object, String>() {
            private multi multi;
            @Override
            public String call(Object renderer) throws Exception {
                if(multi == null) multi = new multi(){};
                return multi.<String>methodOption(renderer).getOrElse(toString.apply(renderer));
            }
            @multimethod String call(Attribute attribute) {return "$" + attribute + "$";}
            @multimethod String call(TemplateCall call) {return "$" + call + "$";}

        }).toString("");
    }
}
