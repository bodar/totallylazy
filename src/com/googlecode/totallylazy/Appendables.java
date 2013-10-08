package com.googlecode.totallylazy;

import java.io.IOException;

import static com.googlecode.totallylazy.LazyException.lazyException;

public class Appendables {
    public static <A extends Appendable> A append(CharSequence charSequence, int start, int end, A appendable) {
        try {
            appendable.append(charSequence, start, end);
            return appendable;
        } catch (IOException e) {
            throw lazyException(e);
        }
    }

    public static <A extends Appendable> A append(CharSequence charSequence, A appendable) {
        try {
            appendable.append(charSequence);
            return appendable;
        } catch (IOException e) {
            throw lazyException(e);
        }
    }

    public static <A extends Appendable> A append(char character, A appendable) {
        try {
            appendable.append(character);
            return appendable;
        } catch (IOException e) {
            throw lazyException(e);
        }
    }
}
