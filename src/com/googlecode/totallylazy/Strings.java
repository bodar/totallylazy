package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.ContainsPredicate;
import com.googlecode.totallylazy.predicates.EndsWithPredicate;
import com.googlecode.totallylazy.predicates.StartsWithPredicate;

import java.io.*;

public class Strings {
    public static Callable1<String, String> toLowerCase() {
        return new Callable1<String, String>() {
            public String call(String value) throws Exception {
                return value.toLowerCase();
            }
        };
    }

    public static Callable1<String, String> toUpperCase() {
        return new Callable1<String, String>() {
            public String call(String value) throws Exception {
                return value.toUpperCase();
            }
        };
    }

    public static Predicate<String> startsWith(final String value) {
        return new StartsWithPredicate(value);
    }

    public static Predicate<String> endsWith(final String value) {
        return new EndsWithPredicate(value);
    }

    public static Predicate<String> contains(final String value) {
        return new ContainsPredicate(value);
    }

    public static Predicate<String> equalIgnoringCase(final String expected) {
        return new Predicate<String>() {
            public boolean matches(String actual) {
                return expected.equalsIgnoreCase(actual);
            }
        };
    }

    public static String escapeXml(String value) {
        return new Escaper().
                withRule('&', "&amp;").
                withRule('<', "&lt;").
                withRule('>', "&gt;").
                withRule('\'', "&apos;").
                withRule(unicodeControlOrUndefinedCharacter(), toXmlEntity()).
                escape(value);
    }

    public static Callable1<Character, String> toXmlEntity() {
        return new Callable1<Character, String>() {
            public String call(Character character) throws Exception {
                return String.format("&#%s;", Integer.toString(character, 10));
            }
        };
    }

    public static Predicate<Character> unicodeControlOrUndefinedCharacter() {
        return new Predicate<Character>() {
            public boolean matches(Character character) {
                return character > 0x7F;
            }
        };
    }

    public static String capitalise(String value) {
        int strLen;
        if (value == null || (strLen = value.length()) == 0) {
            return value;
        }
        return new StringBuffer(strLen)
                .append(Character.toTitleCase(value.charAt(0)))
                .append(value.substring(1))
                .toString();
    }
    
    public static String toString(File file) {
        try {
            return toString(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toString(InputStream stream) {
        if (stream == null) return "";
        return toString(new InputStreamReader(stream));
    }

    public static String toString(Reader reader) {
        try {
            StringBuilder builder = new StringBuilder();
            char[] buffer = new char[512];
            int read = reader.read(buffer);
            while (read > 0) {
                builder.append(buffer, 0, read);
                read = reader.read(buffer);
            }
            return builder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
