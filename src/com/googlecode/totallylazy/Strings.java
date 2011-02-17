package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.ContainsPredicate;
import com.googlecode.totallylazy.predicates.EndsWithPredicate;
import com.googlecode.totallylazy.predicates.StartsWithPredicate;

import java.io.*;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Predicates.notNull;
import static com.googlecode.totallylazy.Sequences.repeat;

public class Strings {
    public static Sequence<String> lines(File file)  {
        try {
            return repeat(readLine(new BufferedReader(new FileReader(file)))).takeWhile(notNull(String.class));
        } catch (FileNotFoundException e) {
            throw new LazyException(e);
        }
    }

    public static Callable<String> readLine(final BufferedReader reader) {
        return new Callable<String>() {
            public String call() throws Exception {
                return reader.readLine();
            }
        };
    }


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

    public static Callable1<String, Predicate<String>> equalIgnoringCase() {
        return new Callable1<String, Predicate<String>>() {
            public Predicate<String> call(String expected) throws Exception {
                return equalIgnoringCase(expected);
            }
        };
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
    
    public static String toString(byte[] bytes) {
        return toString(new ByteArrayInputStream(bytes));
    }

    public static String toString(File file) {
        try {
            return toString(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new LazyException(e);
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
            throw new LazyException(e);
        }
    }
}
