package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.iterators.SegmentIterator;

import static com.googlecode.totallylazy.Characters.identifierPart;
import static com.googlecode.totallylazy.Characters.identifierStart;
import static com.googlecode.totallylazy.parser.CharacterParser.character;

public class Parsers {
    public static Function1<Segment<Character>, String> toString = new Function1<Segment<Character>, String>() {
        @Override
        public String call(Segment<Character> characterSegment) throws Exception {
            return Iterators.toString(new SegmentIterator<Character>(characterSegment), "");
        }
    };

    public static Parser<String> identifier =
            character(identifierStart).
            then(character(identifierPart).many()).
                    map(ManyParser.<Character>cons()).map(toString);

    public static <A> Parser<A> parser(final Parse<? extends A> parser){
        return MappingParser.map(parser, Callables.<A>returnArgument());
    }
}
