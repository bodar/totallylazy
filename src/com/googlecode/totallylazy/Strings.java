package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.ContainsPredicate;
import com.googlecode.totallylazy.predicates.EndsWithPredicate;
import com.googlecode.totallylazy.predicates.StartsWithPredicate;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.Predicates.notNullValue;
import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.totallylazy.Sequences.repeat;

public class Strings {
    public static final String EMPTY = "";

    public static Callable1<? super String, Boolean> asBoolean() {
        return new Callable1<String, Boolean>() {
            public Boolean call(String value) throws Exception {
                return Boolean.parseBoolean(value);
            }
        };
    }

    public static Sequence<String> lines(File file) {
        try {
            return lines(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new LazyException(e);
        }
    }

    public static Sequence<String> lines(InputStream stream) {
        return lines(new InputStreamReader(stream));
    }

    public static Sequence<String> lines(Reader reader) {
        return repeat(readLine(new BufferedReader(reader))).takeWhile(notNullValue(String.class)).memorise();
    }

    public static Callable<String> readLine(final BufferedReader reader) {
        return new Callable<String>() {
            public String call() throws Exception {
                String result = reader.readLine();
                if (result == null) {
                    reader.close();
                }
                return result;
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

    public static Predicate<? super String> empty() {
        return new Predicate<String>() {
            public boolean matches(String value) {
                return isEmpty(value);
            }
        };
    }

    public static boolean isEmpty(String value) {
        return value == null || value.equals(EMPTY);
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

    public static Predicate<? super Character> unicodeControlOrUndefinedCharacter() {
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
            return toString(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new LazyException(e);
        }
    }

    public static String toString(InputStream stream) {
        if (stream == null) return EMPTY;
        return toString(new InputStreamReader(stream));
    }

    public static String toString(Reader reader) {
        return using(reader, new Callable1<Reader, String>() {
            public String call(Reader reader) throws Exception {
                StringBuilder builder = new StringBuilder();
                char[] buffer = new char[512];
                int read = reader.read(buffer);
                while (read > 0) {
                    builder.append(buffer, 0, read);
                    read = reader.read(buffer);
                }
                return builder.toString();
            }
        });
    }

    public static Callable1<Object, String> format(final String format) {
        return new Callable1<Object, String>() {
            public String call(Object value) throws Exception {
                return String.format(format, value);
            }
        };
    }

    public static Callable1<? super CharSequence, Sequence<Character>> toCharacters() {
        return new Callable1<CharSequence, Sequence<Character>>() {
            public Sequence<Character> call(CharSequence value) throws Exception {
                return characters(value);
            }
        };
    }

    public static Callable1<String, String> reverse() {
        return new Callable1<String, String>() {
            public String call(String value) throws Exception {
                return reverse(value);
            }
        };
    }

    public static Callable1<String, String> substring(final int beginIndex, final int endIndex) {
        return new Callable1<String, String>() {
            public String call(String value) throws Exception {
                return substring(value, beginIndex, endIndex);
            }
        };
    }

    public static String reverse(String original) {
        return new StringBuilder(original).reverse().toString();
    }

    public static String substring(String original, int beginIndex, int endIndex) {
        int length = original.length();
        int beginIndexPositive = toPositive(length, beginIndex);
        int endIndexPositive = toPositive(length, endIndex);

        if (beginIndexPositive > endIndexPositive) {
            return substring(reverse(original), length - beginIndexPositive, length - endIndexPositive);
        }
        return original.substring(beginIndexPositive, endIndexPositive);
    }

    private static int toPositive(int stringLength, int index) {
        if (index < 0) {
            return stringLength + index;
        }
        return index;
    }
}
