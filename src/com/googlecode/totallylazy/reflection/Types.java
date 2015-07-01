package com.googlecode.totallylazy.reflection;

import com.googlecode.totallylazy.Objects;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.predicates.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.numbers.Numbers;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Types {
    public static ParameterizedType parameterizedType(final Type rawType, final Type... typeArguments) {
        return new AParameterizedType(null, rawType, typeArguments);
    }

    public static ParameterizedType parameterizedType(final Type rawType, final Iterable<Type> typeArguments) {
        return new AParameterizedType(null, rawType, sequence(typeArguments).toArray(Type.class));
    }

    public static Sequence<Type> classTypeParameters(Type concrete) {
        if (concrete instanceof Class) {
            return sequence(((Class) concrete).getTypeParameters()).safeCast(Type.class);
        }
        if (concrete instanceof ParameterizedType) {
            return sequence(((ParameterizedType) concrete).getActualTypeArguments());
        }
        throw new UnsupportedOperationException();
    }

    public static <T> Class<T> classOf(Type concrete) {
        return Types.<T>classOption(concrete).getOrThrow(new UnsupportedOperationException());
    }

    public static <T> Option<Class<T>> classOption(Type concrete) {
        if (concrete instanceof Class) {
            return some(Unchecked.<Class<T>>cast(concrete));
        }
        if (concrete instanceof ParameterizedType) {
            return some(Types.<T>classOf(((ParameterizedType) concrete).getRawType()));
        }
        return none();
    }

    public static boolean equalTo(Type a, Type b) {
        if (a == null && b == null) {
            return true;
        }

        if (a instanceof ParameterizedType && b instanceof ParameterizedType) {
            return equalTo((ParameterizedType) a, (ParameterizedType) b);
        }

        if (a instanceof WildcardType && b instanceof WildcardType) {
            return equalTo((WildcardType) a, (WildcardType) b);
        }

        return Objects.equalTo(a, b);
    }

    public static boolean equalTo(WildcardType aWildcard, WildcardType bWildcard) {
        return sequence(aWildcard.getUpperBounds()).zip(sequence(bWildcard.getUpperBounds())).forAll(equalTo()) &&
                sequence(aWildcard.getLowerBounds()).zip(sequence(bWildcard.getLowerBounds())).forAll(equalTo());
    }

    public static boolean equalTo(ParameterizedType pa, ParameterizedType pb) {
        return equalTo(pa.getOwnerType(), pb.getOwnerType()) &&
                equalTo(pa.getRawType(), pb.getRawType()) &&
                sequence(pa.getActualTypeArguments()).zip(sequence(pb.getActualTypeArguments())).forAll(equalTo());
    }

    public static Predicate<? super Pair<Type, Type>> equalTo() {
        return pair -> equalTo(pair.first(), pair.second());
    }

    public static Predicate<? super Type> equalTo(final Type type) {
        return other -> equalTo(type, other);
    }

    public static boolean matches(Type concrete, Type possibleWildCard) {
        if (concrete == null && possibleWildCard == null) {
            return true;
        }

        if (concrete instanceof ParameterizedType && possibleWildCard instanceof ParameterizedType) {
            return matches((ParameterizedType) concrete, (ParameterizedType) possibleWildCard);
        }

        if (concrete instanceof WildcardType && possibleWildCard instanceof WildcardType) {
            return matches((WildcardType) concrete, (WildcardType) possibleWildCard);
        }

        if (possibleWildCard instanceof WildcardType) {
            return withinBounds(concrete, (WildcardType) possibleWildCard);
        }

        return Objects.equalTo(concrete, possibleWildCard);
    }

    public static boolean matches(WildcardType aWildcard, WildcardType bWildcard) {
        return sequence(aWildcard.getUpperBounds()).zip(sequence(bWildcard.getUpperBounds())).forAll(matches()) &&
                sequence(aWildcard.getLowerBounds()).zip(sequence(bWildcard.getLowerBounds())).forAll(matches());
    }

    public static boolean matches(ParameterizedType type, ParameterizedType anotherType) {
        return matches(type.getOwnerType(), anotherType.getOwnerType()) &&
                matches(type.getRawType(), anotherType.getRawType()) &&
                sequence(type.getActualTypeArguments()).zip(sequence(anotherType.getActualTypeArguments())).forAll(matches());
    }

    public static boolean withinBounds(Type concrete, WildcardType wildcardType) {
        return withInUpperBounds(concrete, sequence(wildcardType.getUpperBounds())) &&
                withInLowerBounds(concrete, sequence(wildcardType.getLowerBounds()));
    }

    @SuppressWarnings("unchecked")
    public static boolean withInUpperBounds(Type concrete, Sequence<Type> upperBounds) {
        if (upperBounds.isEmpty()) {
            return true;
        }
        if (Numbers.equalTo(upperBounds.size(), 1)) {
            return (classOf(upperBounds.first())).isAssignableFrom(classOf(concrete));
        }
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public static boolean withInLowerBounds(Type concrete, Sequence<Type> lowerBounds) {
        if (lowerBounds.isEmpty()) {
            return true;
        }
        if (Numbers.equalTo(lowerBounds.size(), 1)) {
            return (classOf(concrete)).isAssignableFrom(classOf(lowerBounds.first()));
        }
        throw new UnsupportedOperationException();
    }

    public static Predicate<? super Pair<Type, Type>> matches() {
        return pair -> matches(pair.first(), pair.second());
    }

    public static Predicate<? super Type> matches(final Type type) {
        return other -> matches(type, other);
    }


}
