package com.googlecode.totallylazy;

import com.googlecode.totallylazy.annotations.multimethod;
import com.googlecode.totallylazy.annotations.tailrec;
import com.googlecode.totallylazy.callables.JoinString;
import com.googlecode.totallylazy.comparators.Maximum;
import com.googlecode.totallylazy.comparators.Minimum;
import com.googlecode.totallylazy.predicates.ContainsPredicate;
import com.googlecode.totallylazy.predicates.EndsWithPredicate;
import com.googlecode.totallylazy.predicates.StartsWithPredicate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.LazyException.lazyException;
import static com.googlecode.totallylazy.Predicates.notNullValue;
import static com.googlecode.totallylazy.Predicates.or;
import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Streams.inputStreamReader;

public class Strings {
    public static final String EMPTY = "";

    public static final Charset UTF8 = Charset.forName("UTF-8");
    public static final CombinerFunction<String> join = JoinString.instance;

    public static Function<String, Boolean> asBoolean() {
        return Boolean::parseBoolean;
    }

    public static Sequence<String> lines(File file) {
        try {
            return lines(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw lazyException(e);
        }
    }

    public static Sequence<String> lines(InputStream stream) {
        return lines(inputStreamReader(stream));
    }

    public static Sequence<String> lines(Reader reader) {
        return repeat(readLine(new BufferedReader(reader))).takeWhile(notNullValue(String.class)).memorise();
    }

    public static Returns<String> readLine(final BufferedReader reader) {
        return () -> {
            String result = reader.readLine();
            if (result == null) {
                reader.close();
            }
            return result;
        };
    }

    public static Function<String, String> toLowerCase() {
        return String::toLowerCase;
    }

    public static Function<String, String> replace(final char oldChar, final char newChar) {
        return value -> value.replace(oldChar, newChar);
    }

    public static Function<String, String> replace(final CharSequence target, final CharSequence replacement) {
        return value -> value.replace(target, replacement);
    }

    public static Function<String, String> replaceAll(final String regex, final String replacement) {
        return value -> value.replaceAll(regex, replacement);
    }

    public static Function<String, String> replaceFirst(final String regex, final String replacement) {
        return value -> value.replaceFirst(regex, replacement);
    }

    public static Function<String, String> toUpperCase() {
        return String::toUpperCase;
    }

    public static Predicate<String> startsWith(final String value) {
        return new StartsWithPredicate(value);
    }

    public static Function<String, Predicate<String>> startsWith() {
        return Strings::startsWith;
    }

    public static Predicate<String> startsWith(String first, String... rest) {
        return or(sequence(rest).cons(first).map(startsWith()));
    }

    public static Predicate<String> endsWith(final String value) {
        return new EndsWithPredicate(value);
    }

    public static Function<String, Predicate<String>> endsWith() {
        return Strings::endsWith;
    }

    public static Predicate<String> endsWith(String first, String... rest) {
        return or(sequence(rest).cons(first).map(endsWith()));
    }

    public static Predicate<String> contains(final String value) {
        return new ContainsPredicate(value);
    }

    public static Function<String, Predicate<String>> equalIgnoringCase() {
        return Strings::equalIgnoringCase;
    }

    public static Predicate<String> equalIgnoringCase(final String expected) {
        return expected::equalsIgnoreCase;
    }

    public static Predicate<String> empty = Strings::isEmpty;

    public static Predicate<String> empty() {
        return empty;
    }

    public static Predicate<String> blank = Strings::isBlank;

    public static Predicate<String> blank() {
        return blank;
    }

    public static boolean isEmpty(String value) {
        return value == null || value.equals(EMPTY);
    }

    public static boolean isBlank(String value) {
        return isEmpty(value) || isEmpty(value.trim());
    }

    public static Predicate<Character> unicodeControlOrUndefinedCharacter() {
        return character -> character > 0x7F;
    }

    public static String capitalise(String value) {
        if (isEmpty(value)) return value;
        return String.valueOf(Character.toTitleCase(value.charAt(0))) + value.substring(1);
    }

    public static String asString(Object value) {
        return value == null ? "" : value.toString();
    }

    public static String string(Object value) {
        return new multi(){}.<String>methodOption(value).getOrElse(String.valueOf(value));
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
        return using(stream, (inputStream) -> toString(inputStreamReader(inputStream)));
    }

    public static String toString(Reader reader) {
        return using(reader, (reader1) -> {
            StringBuilder builder = new StringBuilder();
            char[] buffer = new char[512];
            int read = reader.read(buffer);
            while (read > 0) {
                builder.append(buffer, 0, read);
                read = reader.read(buffer);
            }
            return builder.toString();
        });
    }

    public static Function<Object, String> format(final String format) {
        return value -> String.format(format, value);
    }

    public static Function<CharSequence, Sequence<Character>> toCharacters() {
        return Sequences::characters;
    }

    public static Function<String, String> reverse() {
        return Strings::reverse;
    }

    public static Function<String, Sequence<String>> split(final String regex) {
        if (regex == null) throw new IllegalArgumentException("regex cannot be null");
        return s -> sequence(s.split(regex));
    }

    public static Function<String, String> substring(final int beginIndex, final int endIndex) {
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

    public static Function<String, Character> characterAt(final int index) {
        return s -> s.charAt(index);
    }


    public static Function<String, String> trim() {
        return String::trim;
    }

    public static Predicate<String> palindrome = Strings::isPalindrome;

    public static Predicate<String> isPalindrome() {
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
        public static Function3<String, String, String, String> replaceAll = new Function3<String, String, String, String>() {
            @Override
            public String call(String regex, String replacemenent, String source) throws Exception {
                return Strings.replaceAll(regex, replacemenent).apply(source);
            }
        };
    }
}