package com.googlecode.totallylazy.json;

import static com.googlecode.totallylazy.Sequences.characters;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;

public interface Strings {
    static String toString(CharSequence value) {
        return quote(escape(value));
    }

    static String escape(CharSequence value) {
        return characters(value).map(Strings::escape).toString("");
    }

    static String escape(Character character) {
        switch (character) {
            case '"': return "\\\"";
            case '\\': return "\\\\";
            case '\b': return "\\b";
            case '\n': return "\\n";
            case '\r': return "\\r";
            case '\t': return "\\t";
            default: return character.toString();
        }
    }

    static String quote(CharSequence value) {
        return format("\"%s\"", value);
    }

    static String unescape(CharSequence escaped) {
        switch (escaped.charAt(0)) {
            case '"': return "\"";
            case '\\': return "\\";
            case '/': return "/";
            case 'b': return "\b";
            case 'n': return "\n";
            case 'r': return "\r";
            case 't': return "\t";
            case 'f': return "\f";
            case 'u': return Character.toString((char) parseInt(escaped.subSequence(2, escaped.length()).toString(), 16));
            default: throw new UnsupportedOperationException();
        }
    }
}