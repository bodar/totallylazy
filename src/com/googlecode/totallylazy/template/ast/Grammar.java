package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.parser.Parser;
import com.googlecode.totallylazy.parser.Parsers;

import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Characters.alphaNumeric;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.parser.Parsers.between;
import static com.googlecode.totallylazy.parser.Parsers.isChar;
import static com.googlecode.totallylazy.parser.Parsers.ws;
import static com.googlecode.totallylazy.parser.Parsers.wsChar;

public interface Grammar {
    Parser<Void> SEPARATOR = wsChar(',').ignore();
    Parser<Void> SINGLE_QUOTE = isChar('\'').ignore();
    Parser<Void> DOUBLE_QUOTE = isChar('"').ignore();
    Parser<String> IDENTIFIER = Parsers.characters(alphaNumeric).map(Object::toString);

    char del = '$';

    Parser<Attribute> ATTRIBUTE = IDENTIFIER.map(Attribute::new);
    Parser<CharSequence> TEXT = textExcept(del);
    Parser<CharSequence> SINGLE_QUOTED = between(SINGLE_QUOTE, textExcept('\''), SINGLE_QUOTE);
    Parser<CharSequence> DOUBLE_QUOTED = between(DOUBLE_QUOTE, textExcept('"'), DOUBLE_QUOTE);
    Parser<CharSequence> LITERAL = SINGLE_QUOTED.or(DOUBLE_QUOTED);

    static Parser<CharSequence> textExcept(char c) {
        return Parsers.characters(not(c));
    }
    
    Parser<Object> VALUE = Parsers.lazy(() -> ws(Parsers.or(LITERAL, FUNCTION_CALL(), ATTRIBUTE)));

    Parser<Pair<String, Object>> NAMED_ARGUMENT = Parsers.tuple(IDENTIFIER, isChar('='), VALUE).
            map(triple -> Pair.pair(triple.first(), triple.third()));

    Parser<Map<String, Object>> NAMED_ARGUMENTS = NAMED_ARGUMENT.sepBy1(SEPARATOR).map(Maps::map);

    Parser<List<Object>> IMPLICIT_ARGUMENTS = VALUE.sepBy(SEPARATOR);

    Parser<FunctionCall> FUNCTION_CALL =
            Parsers.pair(IDENTIFIER, between(isChar('('), Parsers.or(NAMED_ARGUMENTS, IMPLICIT_ARGUMENTS), isChar(')'))).
            map(pair -> new FunctionCall(pair.first(), pair.second()));

    static Parser<FunctionCall> FUNCTION_CALL() { return FUNCTION_CALL; }

    Parser<Expression> EXPRESSION = Parsers.or(FUNCTION_CALL, ATTRIBUTE).between(isChar(del), isChar(del));

    Parser<List<Object>> TEMPLATE = Parsers.or(EXPRESSION, TEXT).many1();
}