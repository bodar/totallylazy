package com.googlecode.totallylazy;

import com.googlecode.totallylazy.annotations.multimethod;
import com.googlecode.totallylazy.annotations.tailrec;
import com.googlecode.totallylazy.functions.*;
import com.googlecode.totallylazy.comparators.Maximum;
import com.googlecode.totallylazy.comparators.Minimum;
import com.googlecode.totallylazy.predicates.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.LazyException.lazyException;
import static com.googlecode.totallylazy.predicates.Predicates.notNullValue;
import static com.googlecode.totallylazy.predicates.Predicates.or;
import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Streams.inputStreamReader;
import static com.googlecode.totallylazy.Strings.toString;
import static com.googlecode.totallylazy.io.Uri.uri;

public class Strings {
    public static final String EMPTY = "";

    public static final Charset UTF8 = Charset.forName("UTF-8");
    public static final Monoid<String> join = ConcatString.instance;

    public static Function1<String, Boolean> asBoolean() {
        return Boolean::parseBoolean;
    }

    public static Sequence<String> lines(File file) {
        return Streams.lines(file).memorise();
    }

    public static Sequence<String> lines(InputStream stream) {
        return Streams.lines(stream).memorise();
    }

    public static Sequence<String> lines(Reader reader) {
        return Streams.lines(reader).memorise();
    }

    public static Sequence<String> lines(String lines) {
        return Streams.lines(lines).memorise();
    }

    public static Function0<String> readLine(final BufferedReader reader) { return Streams.readLine(reader); }

    public static Function1<String, String> toLowerCase() {
        return String::toLowerCase;
    }

    public static Function1<String, String> replace(final char oldChar, final char newChar) {
        return value -> value.replace(oldChar, newChar);
    }

    public static Function1<String, String> replace(final CharSequence target, final CharSequence replacement) {
        return value -> value.replace(target, replacement);
    }

    public static Function1<String, String> replaceAll(final String regex, final String replacement) {
        return value -> value.replaceAll(regex, replacement);
    }

    public static Function1<String, String> replaceFirst(final String regex, final String replacement) {
        return value -> value.replaceFirst(regex, replacement);
    }

    public static Function1<String, String> toUpperCase() {
        return String::toUpperCase;
    }

    public static LogicalPredicate<String> startsWith(final String value) {
        return new StartsWithPredicate(value);
    }

    public static Function1<String, Predicate<String>> startsWith() {
        return Strings::startsWith;
    }

    public static LogicalPredicate<String> startsWith(String first, String... rest) {
        return or(sequence(rest).cons(first).map(startsWith()));
    }

    public static LogicalPredicate<String> endsWith(final String value) {
        return new EndsWithPredicate(value);
    }

    public static Function1<String, Predicate<String>> endsWith() {
        return Strings::endsWith;
    }

    public static LogicalPredicate<String> endsWith(String first, String... rest) {
        return or(sequence(rest).cons(first).map(endsWith()));
    }

    public static LogicalPredicate<String> contains(final String value) {
        return new ContainsPredicate(value);
    }

    public static Function1<String, Predicate<String>> equalIgnoringCase() {
        return Strings::equalIgnoringCase;
    }

    public static LogicalPredicate<String> equalIgnoringCase(final String expected) {
        return new LogicalPredicate<String>() {
            public boolean matches(String actual) {
                return expected.equalsIgnoreCase(actual);
            }
        };
    }

    public static LogicalPredicate<String> empty = new LogicalPredicate<String>() {
        public boolean matches(String value) {
            return isEmpty(value);
        }
    };

    public static LogicalPredicate<String> empty() {
        return empty;
    }

    public static LogicalPredicate<String> blank = new LogicalPredicate<String>() {
        public boolean matches(String value) {
            return isBlank(value);
        }
    };

    public static LogicalPredicate<String> blank() {
        return blank;
    }

    public static boolean isEmpty(String value) {
        return value == null || value.equals(EMPTY);
    }

    public static boolean isBlank(String value) {
        return isEmpty(value) || isEmpty(value.trim());
    }

    public static LogicalPredicate<Character> unicodeControlOrUndefinedCharacter() {
        return new LogicalPredicate<Character>() {
            public boolean matches(Character character) {
                return character > 0x7F;
            }
        };
    }

    public static String capitalise(String value) {
        if (isEmpty(value)) return value;
        return String.valueOf(Character.toTitleCase(value.charAt(0))) + value.substring(1);
    }

    public static String asString(Object value) {
        return value == null ? "" : value.toString();
    }

    private static multi multi;
    public static String string(Object value) {
        if(multi == null) multi = new multi(){};
        return multi.<String>methodOption(value).getOrElse(String.valueOf(value));
    }

    @multimethod
    public static String string(Void value) {
        return "";
    }

    @multimethod
    public static String string(byte[] value) {
        return toString(value);
    }

    @multimethod
    public static String string(File value) {
        return toString(value);
    }

    @multimethod
    public static String string(InputStream value) {
        return toString(value);
    }

    @multimethod
    public static String string(Reader value) {
        return toString(value);
    }

    public static String toString(byte[] bytes) {
        return new String(bytes, UTF8);
    }

    public static String toString(File file) {
        try {
            return toString(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw lazyException(e);
        }
    }

    public static String toString(final InputStream stream) {
        if (stream == null) return EMPTY;
        return using(stream, inputStream -> toString(inputStreamReader(inputStream)));
    }

    public static String toString(Reader reader) {
        return using(reader, reader1 -> {
            StringBuilder builder = new StringBuilder();
            char[] buffer = new char[512];
            int read = reader1.read(buffer);
            while (read > 0) {
                builder.append(buffer, 0, read);
                read = reader1.read(buffer);
            }
            return builder.toString();
        });
    }

    public static Function1<Object, String> format(final String format) {
        return value -> String.format(format, value);
    }

    public static Function1<CharSequence, Sequence<Character>> toCharacters() {
        return Sequences::characters;
    }

    public static Function1<String, String> reverse() {
        return Strings::reverse;
    }

    public static Function1<String, Sequence<String>> split(final String regex) {
        if (regex == null) throw new IllegalArgumentException("regex cannot be null");
        return s -> sequence(s.split(regex));
    }

    public static Function1<String, String> substring(final int beginIndex, final int endIndex) {
        return value -> substring(value, beginIndex, endIndex);
    }

    public static String reverse(String original) {
        return new StringBuilder(original).reverse().toString();
    }

    @tailrec
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

    public static Function1<String, Character> characterAt(final int index) {
        return s -> s.charAt(index);
    }


    public static Function1<String, String> trim() {
        return String::trim;
    }

    public static LogicalPredicate<String> palindrome = new LogicalPredicate<String>() {
        @Override
        public boolean matches(String other) {
            return Strings.isPalindrome(other);
        }
    };

    public static LogicalPredicate<String> isPalindrome() {
        return palindrome;
    }

    public static boolean isPalindrome(String other) {
        return other.equals(reverse(other));
    }

    public static byte[] bytes(String value) {
        return value.getBytes(UTF8);
    }

    public static Maximum.Function<String> maximum = Maximum.constructors.maximum((String) null);

    public static Minimum.Function<String> minimum = Minimum.constructors.minimum((String) null);

    public static class functions {
        public static Curried3<String, String, String, String> replaceAll =
                (regex, replacemenent, source) -> Strings.replaceAll(regex, replacemenent).apply(source);
    }
}