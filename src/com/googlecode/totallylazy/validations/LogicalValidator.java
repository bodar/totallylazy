package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Seq;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;

public abstract class LogicalValidator<T> extends AbstractValidator<T> {

    public LogicalValidator<T> and(Validator<? super T> validator){
        return Validators.allOf.allOf(this, validator); // Adding static imports to this class crashes javac 1.6
    }

    public LogicalValidator<T> andIfSo(Validator<? super T> validator){
        return Validators.firstFailure.firstFailure(this, validator); // Adding static imports to this class crashes javac 1.6
    }

    public LogicalValidator<T> withMessage(String message){
        return SetFailureMessage.constructors.setFailureMessage(this, message); // Adding static imports to this class crashes javac 1.6
    }

    public LogicalValidator<T> withMessage(Function<? super T,String> message){
        return SetFailureMessage.constructors.setFailureMessage(this, message); // Adding static imports to this class crashes javac 1.6
    }

    public LogicalValidator<T> assigningFailuresTo(String key){
        return AssignFailuresToKey.constructors.assignFailuresToKey(key, this); // Adding static imports to this class crashes javac 1.6
    }

    public LogicalValidator<T> when(Predicate<? super T> predicate){
        return ConditionalValidator.constructors.conditionalValidator(this, predicate); // Adding static imports to this class crashes javac 1.6
    }

    public LogicalValidator<T> or(Validator<? super T> validator) {
        Seq<Validator<? super T>> validators = cast(sequence(this, validator));
        return AnyOfValidator.constructors.anyOf(validators);
    }
}
