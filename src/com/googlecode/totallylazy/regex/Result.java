package com.googlecode.totallylazy.regex;

import com.googlecode.totallylazy.Value;

public interface Result extends Value<CharSequence> {
    boolean isMatch();

    static Result unmatched(CharSequence value) {
        return new Unmatched(value);
    }

    static Result matched(CharSequence value) {
        return new Matched(value);
    }

    class Unmatched extends Value.Type<CharSequence> implements Result {
        Unmatched(CharSequence value) {
            super(value);
        }

        @Override
        public boolean isMatch() {
            return false;
        }
    }

    class Matched extends Value.Type<CharSequence> implements Result {
        Matched(CharSequence value) {
            super(value);
        }

        @Override
        public boolean isMatch() {
            return true;
        }
    }
}
