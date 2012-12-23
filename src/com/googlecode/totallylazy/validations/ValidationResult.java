package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.ReducerFunction;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.collections.PersistentMap;
import com.googlecode.totallylazy.comparators.Comparators;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.identity;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.collections.AVLTree.constructors.avlTree;
import static com.googlecode.totallylazy.collections.TreeMap.constructors.factory;
import static com.googlecode.totallylazy.validations.ValidationResult.constructors.pass;

/**
 * An immutable map of String keys to sequences of failure messages
 */
public class ValidationResult {
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
        return messages.get(key).getOrElse(empty(String.class));
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
        return new ValidationResult(this.messages.put(key, newMessages));
    }

    public ValidationResult add(String key, String message) {
        return add(key, sequence(message));
    }

    public ValidationResult add(Iterable<Pair<String, Iterable<String>>> messages) {
        return sequence(messages).fold(this, ValidationResult.functions.add());
    }

    public ValidationResult remove(String key) {
        return new ValidationResult(messages.remove(key));
    }

    public boolean succeeded() {
        return messages.isEmpty();
    }

    public PersistentMap<String, Sequence<String>> toMap() {
        return messages;
    }

    public static class constructors {

        public static ValidationResult pass() {
            return new ValidationResult(emptyMap);
        }

        public static ValidationResult failure(String message) {
            return failure("value", message);
        }

        public static ValidationResult failure(String key, String message) {
            return new ValidationResult(emptyMap).add(key, message);
        }

    }

    public static class functions {

        public static Function1<ValidationResult, Sequence<String>> allMessages(){
            return new Function1<ValidationResult, Sequence<String>>() {
                @Override
                public Sequence<String> call(ValidationResult result) throws Exception {
                    return result.allMessages();
                }
            };
        }

        public static Function1<ValidationResult, Sequence<String>> messages(final String key){
            return new Function1<ValidationResult, Sequence<String>>() {
                @Override
                public Sequence<String> call(ValidationResult result) throws Exception {
                    return result.messages(key);
                }
            };
        }

        public static Function1<ValidationResult, Sequence<String>> messages(final Object key){
            return new Function1<ValidationResult, Sequence<String>>() {
                @Override
                public Sequence<String> call(ValidationResult result) throws Exception {
                    return result.messages(key);
                }
            };
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
            return new Function1<ValidationResult, ValidationResult>() {
                @Override
                public ValidationResult call(ValidationResult validationResult) throws Exception {
                    return validationResult.assignToKey(key);
                }
            };
        }

        public static Function2<ValidationResult, Iterable<String>, ValidationResult> addWithKey(final String key) {
            return new Function2<ValidationResult, Iterable<String>, ValidationResult>() {
                @Override
                public ValidationResult call(ValidationResult validationResult, Iterable<String> messages) throws Exception {
                    return validationResult.add(key, messages);
                }
            };
        }

        public static Function2<ValidationResult, Pair<String, String>, ValidationResult> addSingleMessage() {
            return new Function2<ValidationResult, Pair<String, String>, ValidationResult>() {
                @Override
                public ValidationResult call(ValidationResult validationResult, Pair<String, String> keyAndMessage) throws Exception {
                    return validationResult.add(keyAndMessage.first(), keyAndMessage.second());
                }
            };
        }

        public static Function2<ValidationResult, String, ValidationResult> addSingleMessageWithKey(final String key) {
            return new Function2<ValidationResult, String, ValidationResult>() {
                @Override
                public ValidationResult call(ValidationResult validationResult, String message) throws Exception {
                    return validationResult.add(key, message);
                }
            };
        }

        public static Function2<ValidationResult, Pair<String, ? extends Iterable<String>>, ValidationResult> add() {
            return new Function2<ValidationResult, Pair<String, ? extends Iterable<String>>, ValidationResult>() {
                @Override
                public ValidationResult call(ValidationResult validationResult, Pair<String, ? extends Iterable<String>> keyAndMessages) throws Exception {
                    return validationResult.add(keyAndMessages.first(), keyAndMessages.second());
                }
            };
        }

        public static ReducerFunction<ValidationResult, ValidationResult> merge() {
            return new ReducerFunction<ValidationResult, ValidationResult>() {
                @Override
                public ValidationResult call(ValidationResult seed, ValidationResult value) throws Exception {
                    return seed.add(Unchecked.<Iterable<Pair<String, Iterable<String>>>>cast(value.messages()));
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
