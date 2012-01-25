package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Mappable;
import com.googlecode.totallylazy.Sequence;

public interface Parser<A> extends Mappable<A, Parser<?>> {
    Result<A> parse(Sequence<Character> sequence) throws Exception;
}
