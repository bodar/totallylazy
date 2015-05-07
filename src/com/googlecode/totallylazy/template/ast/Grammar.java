package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.parser.Parse;
import com.googlecode.totallylazy.parser.Parser;
import com.googlecode.totallylazy.parser.Parsers;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Characters.alphaNumeric;
import static com.googlecode.totallylazy.Characters.among;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.parser.Parsers.between;
import static com.googlecode.totallylazy.parser.Parsers.isChar;
import static com.googlecode.totallylazy.parser.Parsers.or;
import static com.googlecode.totallylazy.parser.Parsers.returns;
import static com.googlecode.totallylazy.parser.Parsers.ws;
import static com.googlecode.totallylazy.parser.Parsers.wsChar;

public interface Grammar {
    Parser<Void> SEPARATOR = wsChar(',').ignore();
    Parser<Void> SINGLE_QUOTE = isChar('\'').ignore();
    Parser<Void> DOUBLE_QUOTE = isChar('"').ignore();

    Parser<String> IDENTIFIER = Parsers.characters(alphaNumeric).map(Object::toString);

    char DELIMETER = '$';

    Parser<Attribute> ATTRIBUTE = IDENTIFIER.map(Attribute::new);
    Parser<CharSequence> TEXT = textExcept(is(DELIMETER).or(is('}')));
    Parser<CharSequence> SINGLE_QUOTED = between(SINGLE_QUOTE, textExcept('\''), SINGLE_QUOTE);
    Parser<CharSequence> DOUBLE_QUOTED = between(DOUBLE_QUOTE, textExcept('"'), DOUBLE_QUOTE);
    Parser<CharSequence> LITERAL = SINGLE_QUOTED.or(DOUBLE_QUOTED);

    static Parser<CharSequence> textExcept(char c) {return textExcept(is(c));}
    static Parser<CharSequence> textExcept(Predicate<Character> predicate) {
        return Parsers.characters(not(predicate));
    }
    
    Parser<Object> VALUE = Parsers.lazy(new Callable<Parse<Object>>() {
        @Override
        public Parse<Object> call() throws Exception {
            return ws(or(LITERAL, FUNCTION_CALL, ATTRIBUTE));
        }
    });

    Parser<Expression> EXPRESSION = Parsers.lazy(new Callable<Parse<Expression>>() {
        @Override
        public Parse<Expression> call() throws Exception {
            return or(FUNCTION_CALL, MAPPING, ATTRIBUTE).surroundedBy(isChar(DELIMETER));
        }
    });

    Parser<Pair<String, Object>> NAMED_ARGUMENT = IDENTIFIER.followedBy(isChar('=')).then(VALUE);

    Parser<Map<String, Object>> NAMED_ARGUMENTS = NAMED_ARGUMENT.sepBy1(SEPARATOR).map(Maps::map);

    Parser<List<Object>> IMPLICIT_ARGUMENTS = VALUE.sepBy(SEPARATOR);

    Parser<FunctionCall> FUNCTION_CALL =
            IDENTIFIER.then(between(isChar('('), or(NAMED_ARGUMENTS, IMPLICIT_ARGUMENTS), isChar(')'))).
            map(pair -> new FunctionCall(pair.first(), pair.second()));

    Parser<List<Object>> TEMPLATE = or(EXPRESSION, TEXT).many1();

    Parser<List<String>> PARAMETERS_NAMES = IDENTIFIER.sepBy1(SEPARATOR);
    Parser<AnonymousTemplate> ANONYMOUS_TEMPLATE = between(wsChar('{'), PARAMETERS_NAMES.followedBy(wsChar('|')).then(TEMPLATE) , wsChar('}')).
            map(pair -> new AnonymousTemplate(pair.first(), pair.second()));

    Parser<Mapping> MAPPING = ATTRIBUTE.followedBy(isChar(':')).then(ANONYMOUS_TEMPLATE).
            map(pair -> new Mapping(pair.first(), pair.second()));

}