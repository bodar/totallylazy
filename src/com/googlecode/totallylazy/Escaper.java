package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Callables.asString;
import static com.googlecode.totallylazy.Function1.returns1;
import static com.googlecode.totallylazy.Predicates.always;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Sequences.characters;

public class Escaper {
    private final Rules<Character, String> rules = Rules.rules();

    public Escaper() {
        rules.add(always(Character.class), asString(Character.class));
    }

    public Escaper withRule(Character appliesTo, final String result) {
        return withRule(is(appliesTo), returns1(result));
    }

    public Escaper withRule(Predicate<? super Character> appliesTo, Callable1<? super Character, ? extends String> action) {
        rules.add(appliesTo, action);
        return this;
    }

    public String escape(Object value) {
        return value == null ? null : characters(value.toString()).map(escape()).toString("");
    }

    private Function1<Character, String> escape() {
        return new Function1<Character, String>() {
            public String call(Character character) throws Exception {
                return rules.apply(character);
            }
        };
    }

}
