package me.draww.superrup.utils;

import java.util.Objects;

@FunctionalInterface
public interface TriPredicate<X, Y, Z> {

    boolean test(X x, Y y, Z z);

    default TriPredicate<X, Y, Z> and(TriPredicate<? super X, ? super Y, ? super Z> other) {
        Objects.requireNonNull(other);
        return (X x, Y y, Z z) -> test(x, y, z) && other.test(x, y, z);
    }

    default TriPredicate<X, Y, Z> negate() {
        return (X x, Y y, Z z) -> !test(x, y, z);
    }

    default TriPredicate<X, Y, Z> or(TriPredicate<? super X, ? super Y, ? super Z> other) {
        Objects.requireNonNull(other);
        return (X x, Y y, Z z) -> test(x, y, z) || other.test(x, y, z);
    }
}
