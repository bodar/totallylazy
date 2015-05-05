package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.parser.Parser;
import com.googlecode.totallylazy.parser.Parsers;
import com.googlecode.totallylazy.template.ast.Attribute;
import com.googlecode.totallylazy.template.ast.Node;
import com.googlecode.totallylazy.template.ast.TemplateCall;
import com.googlecode.totallylazy.template.ast.Text;

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
    Parser<String> IDENTIFIER = Parsers.characters(alphaNumeric);

    char del = '$';

    Parser<Attribute> ATTRIBUTE = IDENTIFIER.map(Attribute::new);
    Parser<Text> TEXT = textExcept(del);
    Parser<Text> SINGLE_QUOTED = between(SINGLE_QUOTE, textExcept('\''), SINGLE_QUOTE);
    Parser<Text> DOUBLE_QUOTED = between(DOUBLE_QUOTE, textExcept('"'), DOUBLE_QUOTE);
    Parser<Text> LITERAL = SINGLE_QUOTED.or(DOUBLE_QUOTED);

    static Parser<Text> textExcept(char c) {
        return Parsers.characters(not(c)).map(Text::new);
    }
    
    Parser<Node> VALUE = Parsers.lazy(() -> ws(Parsers.or(LITERAL, TEMPLATE_CALL(), ATTRIBUTE)));

    Parser<Pair<String, Node>> NAMED_ARGUMENT = Parsers.tuple(IDENTIFIER, isChar('='), VALUE).
            map(triple -> Pair.pair(triple.first(), triple.third()));

    Parser<Map<String, Node>> NAMED_ARGUMENTS = NAMED_ARGUMENT.sepBy1(SEPARATOR).map(Maps::map);

    Parser<Map<String, Node>> IMPLICIT_ARGUMENTS = VALUE.sepBy1(SEPARATOR).
            map(renderers -> Maps.map(Sequences.sequence(renderers).zipWithIndex().map(p -> p.map(Object::toString))));

    Parser<Map<String, Node>> NO_ARGUMENTS = Parsers.constant(Maps.<String, Node>map());

    Parser<TemplateCall> TEMPLATE_CALL =
            Parsers.pair(IDENTIFIER, between(isChar('('), Parsers.or(NAMED_ARGUMENTS, IMPLICIT_ARGUMENTS, NO_ARGUMENTS), isChar(')'))).
            map(pair -> new TemplateCall(pair.first(), pair.second()));

    static Parser<TemplateCall> TEMPLATE_CALL() { return TEMPLATE_CALL; }

    Parser<Node> EXPRESSION = Parsers.or(TEMPLATE_CALL, ATTRIBUTE).between(isChar(del), isChar(del));

    Parser<List<Node>> TEMPLATE = Parsers.or(EXPRESSION, TEXT).many1();


}