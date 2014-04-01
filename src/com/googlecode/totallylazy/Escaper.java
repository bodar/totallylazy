package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Callables.toString;
import static com.googlecode.totallylazy.Functions.returns1;
import static com.googlecode.totallylazy.Predicates.always;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Sequences.characters;

public class Escaper {
    private final Rules<Character, String> rules = Rules.rules();

    public Escaper() {
        rules.addLast(always(Character.class), toString);
    }

    public Escaper withRule(Character appliesTo, final String result) {
        return withRule(is(appliesTo), returns1(result));
    }

    public Escaper withRule(Predicate<? super Character> appliesTo, Function<? super Character, ? extends String> action) {
        rules.addFirst(appliesTo, action);
        return this;
    }

    public String escape(Object value) {
        return value == null ? null : characters(value.toString()).map(escape()).toString("");
    }

    private Function<Character, String> escape() {
        return new Function<Character, String>() {
            public String call(Character character) throws Exception {
                return rules.apply(character);
            }
        };
    }

}
