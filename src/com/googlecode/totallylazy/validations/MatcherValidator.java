package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.Function1;
import org.hamcrest.Matcher;

import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.matchers.Matchers.describeMismatch;
import static com.googlecode.totallylazy.validations.MatcherValidator.constructors.validateMatcher;
import static com.googlecode.totallylazy.validations.ValidationResult.constructors.failure;
import static com.googlecode.totallylazy.validations.ValidationResult.constructors.pass;

public class MatcherValidator<T> extends LogicalValidator<T> {
	private final Matcher<? super T> matcher;
	private final Function1<? super T, String> message;

	private MatcherValidator(Matcher<? super T> matcher, Function1<? super T, String> message) {
		this.matcher = matcher;
		this.message = message;
	}

	@Override
	public ValidationResult validate(T instance) {
		return matcher.matches(instance)
				? pass()
				: failure(call(message, instance));
	}

	public static class constructors {
		public static <T> MatcherValidator<T> validateMatcher(Matcher<? super T> matcher) {
			return new MatcherValidator<T>(matcher, describeMismatch(matcher));
		}
	}

    public static class functions{
        public static <T> Function1<Matcher<? super T>, Validator<T>> matcherAsValidator(Class<T> type){
            return matcherAsValidator();
        }

        public static <T> Function1<Matcher<? super T>, Validator<T>> matcherAsValidator(){
            return new Function1<Matcher<? super T>, Validator<T>>() {
                @Override
                public Validator<T> call(Matcher<? super T> matcher) throws Exception {
                    return validateMatcher(matcher);
                }
            };
        }
    }
}