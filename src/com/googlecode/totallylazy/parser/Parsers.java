package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Characters;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.iterators.SegmentIterator;

import static com.googlecode.totallylazy.parser.CharacterParser.character;

public class Parsers {
    public static AbstractParser<String> IDENTIFIER =
            character(Characters.identifierStart).
            then(character(Characters.identifierPart).many()).
                    map(ManyParser.<Character>cons()).map(asString());

    public static Function1<Segment<Character>, String> asString() {
        return new Function1<Segment<Character>, String>() {
            @Override
            public String call(Segment<Character> characterSegment) throws Exception {
                return Iterators.toString(new SegmentIterator<Character>(characterSegment), "");
            }
        };
    }
}
