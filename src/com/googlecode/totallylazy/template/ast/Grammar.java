package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.parser.Parser;
import com.googlecode.totallylazy.parser.Parsers;
import com.googlecode.totallylazy.predicates.Predicate;

import java.util.List;

import static com.googlecode.totallylazy.Arrays.list;
import static com.googlecode.totallylazy.Characters.alphaNumeric;
import static com.googlecode.totallylazy.parser.Parsers.*;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.predicates.Predicates.not;

public interface Grammar {
    Parser<Void> SEPARATOR = wsChar(',').ignore();
    Parser<Void> SINGLE_QUOTE = isChar('\'').ignore();
    Parser<Void> DOUBLE_QUOTE = isChar('"').ignore();

    Parser<String> IDENTIFIER = Parsers.characters(alphaNumeric).map(Object::toString);

    Parser<Name> NAME = IDENTIFIER.map(Name::name);

    static Parser<Text> TEXT(char delimiter) {
        return textExcept(is(delimiter).or(is('}')));
    }

    Parser<Text> SINGLE_QUOTED = between(SINGLE_QUOTE, textExcept('\''), SINGLE_QUOTE);
    Parser<Text> DOUBLE_QUOTED = between(DOUBLE_QUOTE, textExcept('"'), DOUBLE_QUOTE);
    Parser<Text> LITERAL = SINGLE_QUOTED.or(DOUBLE_QUOTED);

    static Parser<Text> textExcept(char c) {
        return textExcept(is(c));
    }

    static Parser<Text> textExcept(Predicate<Character> predicate) {
        return Parsers.characters(not(predicate)).map(Text::text);
    }

    static Parser<Expression> VALUE(char delimiter) {
        return Parsers.lazy(() -> ws(or(LITERAL, FUNCTION_CALL(delimiter), ATTRIBUTE(delimiter))));
    }

    static Parser<Expression> EXPRESSION(char delimiter) {
        return Parsers.lazy(() -> Parsers.<Expression>or(FUNCTION_CALL(delimiter), MAPPING(delimiter), ATTRIBUTE(delimiter)).surroundedBy(isChar(delimiter)));
    }

    static Parser<Expression> NAMES(char delimiter) {
        return Parsers.lazy(() -> or(NAME, INDIRECTION(delimiter)));
    }

    static Parser<Attribute> ATTRIBUTE(char delimiter) {
        return NAMES(delimiter).sepBy1(isChar('.')).map(Attribute::attribute);
    }

    static Parser<Pair<String, Expression>> NAMED_ARGUMENT(char delimiter) {
        return IDENTIFIER.followedBy(isChar('=')).then(VALUE(delimiter));
    }

    static Parser<NamedArguments> NAMED_ARGUMENTS(char delimiter) {
        return NAMED_ARGUMENT(delimiter).sepBy1(SEPARATOR).map(Maps::map).map(NamedArguments::namedArguments);
    }

    static Parser<ImplicitArguments> IMPLICIT_ARGUMENTS(char delimiter) {
        return VALUE(delimiter).sepBy(SEPARATOR).map(ImplicitArguments::implicitArguments);
    }


    static Parser<FunctionCall> FUNCTION_CALL(char delimiter) {
        return NAMES(delimiter).then(between(isChar('('), or(NAMED_ARGUMENTS(delimiter), IMPLICIT_ARGUMENTS(delimiter)), isChar(')'))).
                map(pair -> FunctionCall.functionCall(pair.first(), pair.second()));
    }

    static Parser<List<Expression>> TEMPLATE() {
        return TEMPLATE('$');
    }

    static Parser<List<Expression>> TEMPLATE(char delimiter) {
        return or(EXPRESSION(delimiter), TEXT(delimiter)).many1();
    }

    Parser<List<String>> PARAMETERS_NAMES = IDENTIFIER.sepBy1(SEPARATOR);

    static Parser<Anonymous> ANONYMOUS_TEMPLATE(char delimiter) {
        return between(wsChar('{'),
                PARAMETERS_NAMES.followedBy(wsChar('|')).optional().map(o -> o.getOrElse(list())).
                        then(TEMPLATE(delimiter)), wsChar('}')).
                map(pair -> Anonymous.anonymous(pair.first(), pair.second()));
    }

    static Parser<Mapping> MAPPING(char delimiter) {
        return ATTRIBUTE(delimiter).followedBy(isChar(':')).then(ANONYMOUS_TEMPLATE(delimiter)).
                map(pair -> Mapping.mapping(pair.first(), pair.second()));
    }

    static Parser<Indirection> INDIRECTION(char delimiter) {
        return Parsers.<Expression>or(ATTRIBUTE(delimiter), ANONYMOUS_TEMPLATE(delimiter), LITERAL).between(isChar('('), isChar(')')).
                map(Indirection::indirection);
    }
}