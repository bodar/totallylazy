package com.googlecode.totallylazy.validations;

import java.util.regex.Pattern;

import static com.googlecode.totallylazy.validations.ValidationResult.constructors.failure;
import static com.googlecode.totallylazy.validations.ValidationResult.constructors.pass;

public class RegexValidator extends LogicalValidator<String> {
    private final Pattern regex;

    private RegexValidator(Pattern regex) {this.regex = regex;}

    @Override
    public ValidationResult validate(String instance) {
        return instance != null && regex.matcher(instance).matches()
                ? pass()
                : failure("Value must match pattern: " + regex.pattern());
    }

    public static class constructors {
        public static RegexValidator matchesRegex(String pattern) {
            return matchesRegex(Pattern.compile(pattern));
        }

        public static RegexValidator matchesRegex(Pattern pattern) {
            return new RegexValidator(pattern);
        }
    }
}
