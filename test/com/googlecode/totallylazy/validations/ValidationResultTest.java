package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.collections.PersistentMap;
import org.junit.Test;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterablePredicates.hasExactly;
import static com.googlecode.totallylazy.matchers.IterablePredicates.isEmpty;
import static com.googlecode.totallylazy.validations.ValidationResult.constructors.failure;
import static com.googlecode.totallylazy.validations.ValidationResult.constructors.pass;
import static com.googlecode.totallylazy.validations.ValidationResult.functions.add;
import static com.googlecode.totallylazy.validations.ValidationResult.functions.addSingleMessageWithKey;
import static com.googlecode.totallylazy.validations.ValidationResult.functions.addWithKey;
import static com.googlecode.totallylazy.validations.ValidationResult.functions.assignToKey;
import static com.googlecode.totallylazy.validations.ValidationResult.functions.merge;
import static com.googlecode.totallylazy.PredicateAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidationResultTest {
    @Test
    public void isImmutable() {
        ValidationResult original= failure("A", "message a");
        original.add("A", "message b");

        assertThat(original.allMessages(), hasExactly("message a"));
    }

    @Test
    public void tellsUsWhetherItSucceeded() {
        assertTrue(pass().succeeded());
        assertFalse(failure("failed").succeeded());
    }

    @Test
    public void exposesAllMessagesForAllKeysAsASequence() {
        ValidationResult result = pass().
                add("A", "message a").
                add("B", "message b");
        
        assertThat(result.allMessages(), hasExactly("message a", "message b"));
    }

    @Test
    public void canConvertToMap() {
        ValidationResult result = pass().
                add("A", "message a 1").
                add("A", "message a 2").
                add("B", "message b 1").
                add("B", "message b 2");

        PersistentMap<String,Sequence<String>> map = result.toMap();
        assertThat(map.lookup("A").get(), hasExactly("message a 1", "message a 2"));
        assertThat(map.lookup("B").get(), hasExactly("message b 1", "message b 2"));
    }

    @Test
    public void canReassignAllMessagesToANewKey() {
        ValidationResult result = pass().
                add("A", "message a").
                add("B", "message b");

        ValidationResult reassigned = result.assignToKey("C");
        assertThat(reassigned.messages("C"), hasExactly("message a", "message b"));

        assertThat(reassigned.messages("A"), isEmpty(String.class));
        assertThat(reassigned.messages("B"), isEmpty(String.class));
    }

    @Test
    public void keysAreCaseSensitive() {
        ValidationResult result = failure("A", "message a");

        assertThat(result.messages("a"), isEmpty(String.class));
        assertThat(result.remove("a").allMessages(), hasExactly("message a"));
    }

    @Test
    public void canGetMessagesByCallingToStringOnAnArbitraryKey() {
        ValidationResult result = failure("A", "message a");

        assertThat(result.messages(new Object(){
            @Override
            public String toString() {
                return "A";
            }
        }), hasExactly("message a"));
    }


    @Test
    public void canRemoveKeys() {
        ValidationResult result = pass().
                add("A", "message a").
                remove("A");

        assertThat(result.allMessages(), isEmpty(String.class));
    }

    @Test
    public void exposesConvenienceFunctions() {
        ValidationResult addPairsOfKeysAndMessages = sequence(pair("A", sequence("message 1", "message 2"))).
                fold(pass(), add());

        assertThat(addPairsOfKeysAndMessages.messages("A"), hasExactly("message 1", "message 2"));


        ValidationResult flatMapMessagesUsingKey = sequence(sequence("message 1"), sequence("message 2")).
                fold(pass(), addWithKey("A"));

        assertThat(flatMapMessagesUsingKey.messages("A"), hasExactly("message 1", "message 2"));


        ValidationResult addSingleMessages = sequence(pair("A", "message 1"), pair("A", "message 2")).
                fold(pass(), ValidationResult.functions.addSingleMessage());

        assertThat(addSingleMessages.messages("A"), hasExactly("message 1", "message 2"));


        ValidationResult addSingleMessagesUsingKey = sequence("message 1", "message 2").
                fold(pass(), addSingleMessageWithKey("A"));

        assertThat(addSingleMessagesUsingKey.messages("A"), hasExactly("message 1", "message 2"));
    }

    @Test
    public void exposesFunctionsToMapManyResultsToANewKeyAndThenMergeThem() {
        ValidationResult resultA = failure("A", "message 1");
        ValidationResult resultB = failure("B", "message 2");

        ValidationResult result = sequence(resultA, resultB).
                map(assignToKey("C")).
                reduce(merge());

        assertThat(result.messages("C"), hasExactly("message 1", "message 2"));
    }

    @Test
    public void shouldStillPassIfThereAreZeroLengthMessagesAssignedToAKey() {
        assertTrue(pass().add("key", empty(String.class)).succeeded());
    }
}
