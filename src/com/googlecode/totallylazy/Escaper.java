package com.googlecode.totallylazy;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.googlecode.totallylazy.Callables.asString;
import static com.googlecode.totallylazy.Function1.returns1;
import static com.googlecode.totallylazy.Predicates.always;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Escaper {
    private final Deque<Rule<Character, Character, String>> rules = new ArrayDeque<Rule<Character, Character, String>>();

    public Escaper() {
        rules.add(Rule.<Character, Character, String>rule(always(Character.class), asString(Character.class)));
    }

    public Escaper withRule(Character appliesTo, final String result) {
        return withRule(is(appliesTo), returns1(result));
    }

    public Escaper withRule(Predicate<? super Character> appliesTo, Callable1<? super Character, ? extends String> action) {
        rules.addFirst(Rule.<Character, Character, String>rule(appliesTo, action));
        return this;
    }

    public String escape(Object value) {
        return value == null ? null : characters(value.toString()).map(escape()).toString("", "", "", Long.MAX_VALUE);
    }

    private Function1<Character, String> escape() {
        return new Function1<Character, String>() {
            public String call(Character character) throws Exception {
                return sequence(rules).
                        filter(Predicates.matches(character)).
                        head().
                        call(character);
            }
        };
    }

}
