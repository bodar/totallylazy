package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.util.NoSuchElementException;

public abstract class Either<L, R> implements Iterable<R>, Value<Object>,  Functor<R>, Applicative<R>, Foldable<R> {
    public static <L, R> Either<L, R> right(R value) {
        return Right.right(value);
    }

    public static <L, R> Either<L, R> right(Class<L> leftType, R value) {
        return Right.right(value);
    }

    public static <L, R> Either<L, R> left(L value) {
        return Left.left(value);
    }

    public static <L, R> Either<L, R> left(L value, Class<R> rightType) {
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
        return either.flatMap(Either.<L, R>identity());
    }

    public static <L, R> Function1<Either<L, R>, Either<L, R>> identity(Class<L> lClass, Class<R> rClass) { return identity(); }

    public static <L, R> Function1<Either<L, R>, Either<L, R>> identity() { return Functions.identity(); }

    public abstract Object value();

    public <Ro> Either<L, Ro> applicate(Either<L, ? extends Callable1<? super R, ? extends Ro>> applicator) {
        return applicate(applicator, this);
    }

    public static <L, Ri, Ro> Either<L, Ro> applicate(Either<L, ? extends Callable1<? super Ri, ? extends Ro>> applicator, Either<L, ? extends Ri> value) {
        if (applicator.isLeft()) return left(applicator.left());
        return value.map(applicator.right());
    }

    public static class functions {
        public static <L, R> Function1<L, Either<L, R>> asLeft() {
            return new Function1<L, Either<L, R>>() {
                @Override
                public Either<L, R> call(L value) throws Exception {
                    return Either.left(value);
                }
            };
        }

        public static <L, R> Function1<R, Either<L, R>> asRight() {
            return new Function1<R, Either<L, R>>() {
                @Override
                public Either<L, R> call(R value) throws Exception {
                    return Either.right(value);
                }
            };
        }

        public static LogicalPredicate<Either<?, ?>> left() {
            return new LogicalPredicate<Either<?, ?>>() {
                @Override
                public boolean matches(Either<?, ?> other) {
                    return other.isLeft();
                }
            };
        }

        public static LogicalPredicate<Either<?, ?>> right() {
            return new LogicalPredicate<Either<?, ?>>() {
                @Override
                public boolean matches(Either<?, ?> other) {
                    return other.isRight();
                }
            };
        }
    }
}
