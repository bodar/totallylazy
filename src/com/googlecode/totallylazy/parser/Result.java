package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Mappable;
import com.googlecode.totallylazy.Value;

public interface Result<A> extends Value<A>, Mappable<A, Result<?>> {
}
