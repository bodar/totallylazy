package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Mappable;
import com.googlecode.totallylazy.Sequence;

public interface Parser<A> extends Callable1<Sequence<Character>, Result<A>>, Mappable<A, Parser<?>>{
}
