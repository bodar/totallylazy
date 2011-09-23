package com.googlecode.totallylazy;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.googlecode.totallylazy.Callables.asString;
import static com.googlecode.totallylazy.Callables.ignoreAndReturn;
import static com.googlecode.totallylazy.Predicates.always;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Escaper {
    private final Deque<Rule> rules = new ArrayDeque<Rule>();

    public Escaper() {
        rules.add(Rule.rule(always(Character.class), asString(Character.class)));
    }

    public Escaper withRule(Character appliesTo, final String result) {
        return withRule(is(appliesTo), ignoreAndReturn(result));
    }

    public Escaper withRule(Predicate<? super Character> appliesTo, Callable1<? super Character, String> action) {
        rules.addFirst(Rule.rule(appliesTo, action));
        return this;
    }

    public String escape(CharSequence value) {
        return value == null ? null : characters(value).map(escape()).toString("", "", "", Long.MAX_VALUE);
    }

    private Callable1<? super Character, String> escape() {
        return new Callable1<Character, String>() {
            public String call(Character character) throws Exception {
                return sequence(rules).
                        filter(Predicates.matches(character)).
                        head().
                        call(character);
            }
        };
    }

    private static class Rule implements Predicate<Character>, Callable1<Character, String> {
        private final Predicate<? super Character> condition;
        private final Callable1<? super Character, String> escape;

        private Rule(Predicate<? super Character> condition, Callable1<? super Character, String> escape) {
            this.condition = condition;
            this.escape = escape;
        }

        private static Rule rule(Predicate<? super Character> condition, Callable1<? super Character, String> escape) {
            return new Rule(condition, escape);
        }

        public boolean matches(Character character) {
            return condition.matches(character);
        }

        public String call(Character character) throws Exception {
            return escape.call(character);
        }
    }
}
