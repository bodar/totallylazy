package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.CombinerFunction;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.CurriedFunction2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.collections.PersistentMap;
import com.googlecode.totallylazy.comparators.Comparators;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import static com.googlecode.totallylazy.Objects.equalTo;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.identity;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.collections.AVLTree.constructors.avlTree;
import static com.googlecode.totallylazy.collections.AVLTree.constructors.factory;
import static com.googlecode.totallylazy.validations.ValidationResult.constructors.pass;
import static java.lang.String.format;

/**
 * An immutable map of String keys to sequences of failure messages
 */
public class ValidationResult {
    public static final String DEFAULT_KEY = "value";
    private final PersistentMap<String, Sequence<String>> messages;

    public ValidationResult(PersistentMap<String, Sequence<String>> messages) {
        this.messages = messages;
    }

    public ValidationResult assignToKey(String key) {
        return new ValidationResult(avlTree(key, allMessages()));
    }

    public Sequence<Pair<String, Sequence<String>>> messages() {
        return sequence(messages);
    }

    public Sequence<String> messages(String key) {
        return messages.lookup(key).getOrElse(empty(String.class));
    }

    /**
     * Calls key.toString() and returns messages for that string
     */
    public Sequence<String> messages(Object key) {
        return messages(key.toString());
    }

    public Sequence<String> allMessages() {
        return sequence(messages).
                map(Callables.<Sequence<String>>second()).
                flatMap(identity(String.class));
    }

    public ValidationResult add(String key, Iterable<String> messages) {
        Sequence<String> newMessages = messages(key).join(messages);
        if(newMessages.isEmpty())
            return this;
        return new ValidationResult(this.messages.insert(key, newMessages));
    }

    public ValidationResult add(String key, String message) {
        return add(key, sequence(message));
    }

    public ValidationResult add(Iterable<Pair<String, Iterable<String>>> messages) {
        return sequence(messages).fold(this, ValidationResult.functions.add());
    }

    public ValidationResult merge(ValidationResult value) {
        return this.add(Unchecked.<Iterable<Pair<String, Iterable<String>>>>cast(value.messages()));
    }

    public ValidationResult remove(String key) {
        return new ValidationResult(messages.delete(key));
    }

    public boolean succeeded() {
        return allMessages().isEmpty();
    }

    public boolean failed() {
        return !succeeded();
    }

    @Override
    public String toString() {
        return format("%s%s", succeeded()? "success" : "failure", messages);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ValidationResult && equalTo(((ValidationResult) o).messages, messages);
    }

    @Override
    public int hashCode() {
        return messages == null ? 19 : messages.hashCode();
    }

    public PersistentMap<String, Sequence<String>> toMap() {
        return messages;
    }
    public static class constructors {

        public static ValidationResult pass() {
            return new ValidationResult(emptyMap);
        }

        public static ValidationResult failure(String message) {
            return failure(DEFAULT_KEY, message);
        }

        public static ValidationResult failure(String key, String message) {
            return pass().add(key, message);
        }
    }

    public static class functions {

        public static Function1<ValidationResult, Sequence<String>> allMessages(){
            return ValidationResult::allMessages;
        }

        public static Function1<ValidationResult, Sequence<String>> messages(final String key){
            return result -> result.messages(key);
        }

        public static Function1<ValidationResult, Sequence<String>> messages(final Object key){
            return result -> result.messages(key);
        }

        public static LogicalPredicate<ValidationResult> succeeded() {
            return new LogicalPredicate<ValidationResult>() {
                @Override
                public boolean matches(ValidationResult other) {
                    return other.succeeded();
                }
            };
        }
        public static Function1<ValidationResult, ValidationResult> assignToKey(final String key) {
            return validationResult -> validationResult.assignToKey(key);
        }

        public static CurriedFunction2<ValidationResult, Iterable<String>, ValidationResult> addWithKey(final String key) {
            return (validationResult, messages1) -> validationResult.add(key, messages1);
        }

        public static CurriedFunction2<ValidationResult, Pair<String, String>, ValidationResult> addSingleMessage() {
            return (validationResult, keyAndMessage) -> validationResult.add(keyAndMessage.first(), keyAndMessage.second());
        }

        public static CurriedFunction2<ValidationResult, String, ValidationResult> addSingleMessageWithKey(final String key) {
            return (validationResult, message) -> validationResult.add(key, message);
        }

        public static CurriedFunction2<ValidationResult, Pair<String, ? extends Iterable<String>>, ValidationResult> add() {
            return (validationResult, keyAndMessages) -> validationResult.add(keyAndMessages.first(), keyAndMessages.second());
        }

        public static CombinerFunction<ValidationResult> merge() {
            return new CombinerFunction<ValidationResult>() {
                @Override
                public ValidationResult call(ValidationResult seed, ValidationResult value) throws Exception {
                    return seed.merge(value);
                }

                @Override
                public ValidationResult identity() {
                    return pass();
                }
            };
        }

    }

    private static final PersistentMap<String, Sequence<String>> emptyMap  = factory.create(Comparators.<String>ascending());
}
