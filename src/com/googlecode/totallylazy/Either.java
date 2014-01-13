package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.LogicalPredicate;

public abstract class Either<L, R> implements Iterable<R>, Value<Object>, Functor<R>, Applicative<R>, Monad<R>, Foldable<R> {
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

    public abstract boolean isRight();

    public abstract boolean isLeft();

    public abstract R right();

    public abstract L left();

    public abstract Either<R, L> flip();

    public abstract <S> S fold(final S seed, final Callable2<? super S, ? super L, ? extends S> left, final Callable2<? super S, ? super R, ? extends S> right);

    public abstract <S> S map(final Function<? super L, S> left, final Function<? super R, ? extends S> right);

    @Override
    public abstract <S> Either<L, S> map(Function<? super R, ? extends S> callable);

    public abstract <S> Either<L, S> flatMap(Function<? super R, ? extends Either<L, S>> callable);

    public static <L, R> Either<L, R> flatten(final Either<L, Either<L, R>> either) {
        return either.flatMap(Either.<L, R>identity());
    }

    public static <L, R> Function1<Either<L, R>, Either<L, R>> identity(Class<L> lClass, Class<R> rClass) {
        return identity();
    }

    public static <L, R> Function1<Either<L, R>, Either<L, R>> identity() { return Functions.identity(); }

    public abstract Object value();

    public <Ro> Either<L, Ro> applicate(Either<L, ? extends Function<? super R, ? extends Ro>> applicator) {
        return applicate(applicator, this);
    }

    public static <L, Ri, Ro> Either<L, Ro> applicate(Either<L, ? extends Function<? super Ri, ? extends Ro>> applicator, Either<L, ? extends Ri> value) {
        if (applicator.isLeft()) return left(applicator.left());
        return value.map(applicator.right());
    }

    public abstract Option<L> leftOption();

    public abstract Option<R> rightOption();

    public static class predicates {
        public static LogicalPredicate<Either<?, ?>> left = new LogicalPredicate<Either<?, ?>>() {
            @Override
            public boolean matches(Either<?, ?> other) {
                return other.isLeft();
            }
        };

        public static LogicalPredicate<Either<?, ?>> right = new LogicalPredicate<Either<?, ?>>() {
            @Override
            public boolean matches(Either<?, ?> other) {
                return other.isRight();
            }
        };
    }

    public static class functions {
        public static <L> Mapper<Either<? extends L, ?>, L> left() {
            return new Mapper<Either<? extends L, ?>, L>() {
                @Override
                public L call(Either<? extends L, ?> either) throws Exception {
                    return either.left();
                }
            };
        }

        public static <R> Mapper<Either<?, ? extends R>, R> right() {
            return new Mapper<Either<?, ? extends R>, R>() {
                @Override
                public R call(Either<?, ? extends R> either) throws Exception {
                    return either.right();
                }
            };
        }

        public static <L> Mapper<Either<? extends L, ?>, Option<? extends L>> leftOption() {
            return new Mapper<Either<? extends L, ?>, Option<? extends L>>() {
                @Override
                public Option<? extends L> call(Either<? extends L, ?> either) throws Exception {
                    return either.leftOption();
                }
            };
        }

        public static <R> Mapper<Either<?, ? extends R>, Option<? extends R>> rightOption() {
            return new Mapper<Either<?, ? extends R>, Option<? extends R>>() {
                @Override
                public Option<? extends R> call(Either<?, ? extends R> either) throws Exception {
                    return either.rightOption();
                }
            };
        }

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

        public static <L, R> Mapper<Either<L, R>, Either<R, L>> flip() {
            return new Mapper<Either<L, R>, Either<R, L>>() {
                @Override
                public Either<R, L> call(Either<L, R> either) throws Exception {
                    return either.flip();
                }
            };
        }


    }
}
