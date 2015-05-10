package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Characters;
import com.googlecode.totallylazy.Value;
import com.googlecode.totallylazy.annotations.multimethod;

public class Text extends Value.Type<CharSequence> implements Expression {
    private Text(CharSequence value) { super(value); }

    public static Text text(CharSequence value) {return new Text(value);}

    @multimethod
    public boolean equals(Text other) {
        return Characters.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Characters.hashCode(value);
    }
}
