package com.googlecode.totallylazy;

import com.googlecode.totallylazy.functions.CurriedFunction2;
import com.googlecode.totallylazy.functions.Function1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Sequences.sequence;
import static java.lang.String.format;

public class Properties {
    public static java.util.Properties properties(InputStream stream) throws IOException {
        java.util.Properties properties = new java.util.Properties();
        try {
            properties.load(stream);
        } finally {
            stream.close();
        }
        return properties;
    }

    public static java.util.Properties properties(String values) {
        java.util.Properties properties = new java.util.Properties();
        try {
            properties.load(new ByteArrayInputStream(values.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException("This should be impossible", e);
        }
        return properties;
    }

    public static java.util.Properties properties(Iterable<Pair<String, String>> values) {
        java.util.Properties properties = new java.util.Properties();
        for (Pair<String, String> value : values) {
            properties.setProperty(value.first(), value.second());
        }
        return properties;
    }

    @SafeVarargs
    public static java.util.Properties properties(Pair<String, String>... values) {
        return properties(sequence(values));
    }

    public static java.util.Properties copy(Map properties) {
        return properties(pairs(properties));
    }

    public static Sequence<Pair<String, String>> pairs(Map properties){
        return Maps.pairs(Unchecked.<Map<String, String>>cast(properties));
    }

    public static java.util.Properties compose(java.util.Properties... properties) {
        return compose(sequence(properties));
    }

    public static java.util.Properties compose(Iterable<java.util.Properties> propertiesSequence) {
        return sequence(propertiesSequence)
                .flatMap(Maps.entries())
                .fold(new java.util.Properties(), setProperty());
    }

    public static CurriedFunction2<java.util.Properties, Map.Entry<Object, Object>, java.util.Properties> setProperty() {
        return (properties, property) -> {
            properties.put(property.getKey(), property.getValue());
            return properties;
        };
    }

    public static CurriedFunction2<java.util.Properties, java.util.Properties, java.util.Properties> compose() {
        return (soFar, nextProperties) -> compose(soFar, nextProperties);
    }

    public static String asString(java.util.Properties properties) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            properties.store(out, "");
        } catch (IOException e) {
            throw new RuntimeException("Problem writing to ByteArrayOutputStream. This should never happen", e);
        }
        return out.toString();
    }

    public static Function1<String, java.util.Properties> propertiesFromString() {
        return Properties::properties;
    }

    public static String expectProperty(java.util.Properties properties, String name) {
        if (!properties.containsKey(name)) {
            throw new NoSuchElementException(format("Expected property '%s' to be defined", name));
        }
        return properties.getProperty(name);
    }
}
