package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.Callers;
import com.googlecode.totallylazy.functions.Functions;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.functions.Unary;

import static com.googlecode.totallylazy.functions.Functions.returns1;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.validations.ValidationResult.constructors.pass;
import static com.googlecode.totallylazy.validations.ValidationResult.functions.add;

public class SetFailureMessage<T> extends LogicalValidator<T> {
    private final Validator<? super T> decorated;
    private final Function1<? super T, String> message;

    public SetFailureMessage(Validator<? super T> decorated, Function1<? super T, String> message) {
        this.decorated = decorated;
        this.message = message;
    }

    @Override
    public ValidationResult validate(T instance) {
        Sequence<Pair<String, Sequence<String>>> messages = decorated.validate(instance).messages();
        return messages.
                map(overrideMessages(instance, message)).
                fold(pass(), add());
    }

    private static <T> Unary<Pair<String, Sequence<String>>> overrideMessages(final T instance, final Function1<T, String> messageBuilder) {
        return keyAndMessages -> {
            String message1 = Callers.call(messageBuilder, instance);
            return pair(keyAndMessages.first(), keyAndMessages.second().map(returns1(message1)));
        };
    }

    public static class constructors{
        public static <T> SetFailureMessage<T> setFailureMessage(Validator<? super T> decorated, String message){
            return setFailureMessage(decorated, Functions.<T, String>returns1(message));
        }

        public static <T> SetFailureMessage<T> setFailureMessage(Validator<? super T> decorated, Function1<? super T, String> message){
            return new SetFailureMessage<T>(decorated,message);
        }
    }
}
