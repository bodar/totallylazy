package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.parser.Parser;
import com.googlecode.totallylazy.parser.Parsers;

import java.util.Map;

import static com.googlecode.totallylazy.Characters.alphaNumeric;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.parser.Parsers.between;
import static com.googlecode.totallylazy.parser.Parsers.isChar;
import static com.googlecode.totallylazy.parser.Parsers.or;
import static com.googlecode.totallylazy.parser.Parsers.ws;
import static com.googlecode.totallylazy.parser.Parsers.wsChar;

class Grammar {
    static final Parser<Void> SEPARATOR = wsChar(',').ignore();
    static final Parser<Void> SINGLE_QUOTE = isChar('\'').ignore();
    static final Parser<Void> DOUBLE_QUOTE = isChar('"').ignore();
    static final Parser<String> IDENTIFIER = Parsers.characters(alphaNumeric);

    final Funclate funclate;
    final char del = '$';

    Grammar(Funclate funclate) {
        this.funclate = funclate;
    }

    Funclate funclate() { return funclate; }

    final Parser<Attribute> ATTRIBUTE = IDENTIFIER.map(name -> new Attribute(name, funclate()));
    final Parser<Text> TEXT = textExcept(del);
    final Parser<Text> SINGLE_QUOTED = between(SINGLE_QUOTE, textExcept('\''), SINGLE_QUOTE);
    final Parser<Text> DOUBLE_QUOTED = between(DOUBLE_QUOTE, textExcept('"'), DOUBLE_QUOTE);
    final Parser<Text> LITERAL = SINGLE_QUOTED.or(DOUBLE_QUOTED);

    Parser<Text> textExcept(char c) {
        return Parsers.characters(not(c)).map(Text::new);
    }

    final Parser<Renderer<Map<String, Object>>> VALUE = Parsers.lazy(() -> ws(or(LITERAL, TEMPLATE_CALL(), ATTRIBUTE)));

    final Parser<Pair<String, Renderer<Map<String, Object>>>> NAMED_ARGUMENT = Parsers.tuple(IDENTIFIER, isChar('='), VALUE).
            map(triple -> Pair.pair(triple.first(), triple.third()));

    final Parser<Map<String, Renderer<Map<String, Object>>>> NAMED_ARGUMENTS = NAMED_ARGUMENT.sepBy1(SEPARATOR).map(Maps::map);

    final Parser<Map<String, Renderer<Map<String, Object>>>> IMPLICIT_ARGUMENTS = VALUE.sepBy1(SEPARATOR).
            map(renderers -> Maps.map(Sequences.sequence(renderers).zipWithIndex().map(p -> p.map(Object::toString))));

    final Parser<Map<String, Renderer<Map<String, Object>>>> NO_ARGUMENTS = Parsers.constant(Maps.<String, Renderer<Map<String, Object>>>map());

    final Parser<TemplateCall> TEMPLATE_CALL =
            Parsers.pair(IDENTIFIER, between(isChar('('), Parsers.or(NAMED_ARGUMENTS, IMPLICIT_ARGUMENTS, NO_ARGUMENTS), isChar(')'))).
            map(pair -> new TemplateCall(pair.first(), pair.second(), funclate()));

    Parser<TemplateCall> TEMPLATE_CALL() { return TEMPLATE_CALL; }

    final Parser<Renderer<Map<String, Object>>> EXPRESSION = Parsers.or(TEMPLATE_CALL, ATTRIBUTE).between(isChar(del), isChar(del));

    final Parser<Template> TEMPLATE = Parsers.or(EXPRESSION, TEXT).many().map(Template::new);

    Template parse(CharSequence charSequence) {
        return TEMPLATE.parse(charSequence).value();
    }

}