package com.googlecode.totallylazy.template.ast;

public class Text implements Expression {
    private CharSequence charSequence;

    public Text(CharSequence charSequence) {
        this.charSequence = charSequence;
    }

    public CharSequence charSequence() {
        return charSequence;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + charSequence() + ")";
    }
}
