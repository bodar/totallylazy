package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Either.predicates.left;
import static com.googlecode.totallylazy.Either.left;
import static com.googlecode.totallylazy.Either.right;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Sequences.sequence;

public interface Monad {

    class methods {
        public static <L, R> Either<Sequence<L>, Sequence<R>> sequenceEs(Iterable<? extends Either<? extends L, ? extends R>> iterable) {
            Sequence<L> errors = sequence(iterable).flatMap(Either.functions.<L>leftOption());
            if (!errors.isEmpty()) return left(errors);
            return right(Sequences.<R>flatten(iterable));
        }

        public static <L, R> Either<L, Sequence<R>> sequenceE(Iterable<? extends Either<? extends L, ? extends R>> iterable) {
            Option<Either<? extends L, ? extends R>> error = sequence(iterable).find(left);
            if (!error.isEmpty()) return left(error.get().left());
            return right(Sequences.<R>flatten(iterable));
        }

        public static <A> Option<Sequence<A>> sequenceO(Iterable<? extends Option<? extends A>> iterable) {
            if (sequence(iterable).contains(Option.<A>none())) return none();
            return some(Sequences.<A>flatten(iterable));
        }
    }
}
