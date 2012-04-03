package com.googlecode.totallylazy;

import java.util.NoSuchElementException;

public abstract class Either<L, R> implements Mappable<R, Either<L, ?>> {
    public static <L, R> Either<L, R> right(R value) {
        return Right.right(value);
    }

    public static <L, R> Either<L, R> left(L value) {
        return Left.left(value);
    }

    public boolean isRight() {
        return false;
    }

    public boolean isLeft() {
        return false;
    }

    public R right() {
        throw new NoSuchElementException();
    }

    public L left() {
        throw new NoSuchElementException();
    }

    public abstract <S> S fold(final S seed, final Callable2<? super S, ? super L, ? extends S> left, final Callable2<? super S, ? super R, ? extends S> right);

    public abstract <S> S map(final Callable1<? super L, S> left, final Callable1<? super R, ? extends S> right);

    @Override
    public abstract <S> Either<L, S> map(Callable1<? super R, ? extends S> callable);

    public abstract <S> Either<L, S> flatMap(Callable1<? super R, ? extends Either<L, S>> callable);

    public static <L, R> Either<L, R> flatten(final Either<L, Either<L, R>> either) {
        return either.flatMap(Function1.<Either<L, R>>identity());
    }

    public abstract Object value();
}
