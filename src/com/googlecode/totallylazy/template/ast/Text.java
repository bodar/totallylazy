package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Objects;
import com.googlecode.totallylazy.Value;
import com.googlecode.totallylazy.annotations.multimethod;

public class Text implements Value<CharSequence>, Expression {
    private final CharSequence value;

    private Text(CharSequence value) { this.value = value.toString(); }

    public static Text text(CharSequence value) {return new Text(value);}

    @Override
    public CharSequence value() {
        return value;
    }

    @multimethod
    @Override
    public boolean equals(Object other) {
        return other instanceof Text && Objects.equalTo(value, ((Text) other).value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
