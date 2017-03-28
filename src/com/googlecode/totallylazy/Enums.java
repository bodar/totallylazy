package com.googlecode.totallylazy;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.reflection.Methods;

import static com.googlecode.totallylazy.Exceptions.optional;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.predicates.Predicates.and;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.predicates.Predicates.where;
import static com.googlecode.totallylazy.reflection.Methods.allMethods;
import static com.googlecode.totallylazy.reflection.Methods.invokeOn;
import static com.googlecode.totallylazy.reflection.Methods.modifier;
import static com.googlecode.totallylazy.reflection.Methods.returnType;
import static com.googlecode.totallylazy.reflection.Types.matches;
import static java.lang.reflect.Modifier.STATIC;

public class Enums {
    public static <T extends Enum<T>> Function1<T, String> name() {
        return anEnum -> anEnum.name();
    }

    public static <T extends Enum<T>> Function1<T, String> name(Class<T> aClass) {
        return name();
    }

    public static <T extends Enum<T>> T valueOf(Class<T> actualType, String value) {
        return actualType.cast(allMethods(actualType).
                filter(and(modifier(STATIC),
                        where(returnType(), matches(actualType)),
                        where(m -> sequence(m.getParameterTypes()), is(sequence(String.class))))).
                pick(optional(invokeOn(null, value))));
    }
}
