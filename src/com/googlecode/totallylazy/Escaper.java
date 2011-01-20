package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Callables.asString;
import static com.googlecode.totallylazy.Predicates.always;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.totallylazy.Sequences.sequence;

import java.util.ArrayList;
import java.util.List;

public class Escaper {
    private final List<Rule> escapeRules = new ArrayList<Rule>();

    public Escaper() {
        escapeRules.add(new Rule(always(Character.class), asString(Character.class)));
    }

    public Escaper withRule(Character appliesTo, final String returns) {
        return withRule(is(appliesTo), returns(returns));
    }

    public Escaper withRule(Predicate<Character> appliesTo, Callable1<Character, String> action) {
        escapeRules.add(new Rule(appliesTo, action));
        return this;
    }

    public String escape(String value) {
        return value == null ? null : characters(value).map(escape()).toString("");
    }

    private Callable1<? super Character, String> escape() {
        return new Callable1<Character, String>() {
            public String call(Character character) throws Exception {
                Rule matchingRule = sequence(escapeRules).
                        reverse().
                        filter(Rule.appliesTo(character)).
                        head();
                return matchingRule.call(character);
            }
        };
    }

    private Callable1<Character, String> returns(final String returns) {
        return new Callable1<Character, String>() {
            public String call(Character character) throws Exception {
                return returns;
            }
        };
    }

    private static class Rule implements Predicate<Character>, Callable1<Character, String> {

        Predicate<Character> condition;

        Callable1<Character, String> escape;

        private Rule(Predicate<Character> condition, Callable1<Character, String> escape) {
            this.condition = condition;
            this.escape = escape;
        }

        public boolean matches(Character character) {
            return condition.matches(character);
        }

        public String call(Character character) throws Exception {
            return escape.call(character);
        }

        public static Predicate<? super Rule> appliesTo(final Character character) {
            return new Predicate<Rule>() {
                public boolean matches(Rule rule) {
                    return rule.matches(character);
                }
            };
        }

    }
}
