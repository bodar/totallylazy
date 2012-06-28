package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.Function.returns;

public class Success<A> extends Pair<A, Segment<Character>> implements Result<A>{
    private Success(A value, Segment<Character> second) {
        super(returns(value), returns(second));
    }

    public static <A> Success<A> success(A value, Segment<Character> second) {
        return new Success<A>(value, second);
    }

    @Override
    public <S> Success<S> map(Callable1<? super A, ? extends S> callable) {
        return success(Function1.call(callable, value()), second());
    }

    public Segment<Character> remainder() {
        return second();
    }
}
