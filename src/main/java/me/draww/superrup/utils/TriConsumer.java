package me.draww.superrup.utils;

import java.util.Objects;

@FunctionalInterface
public interface TriConsumer<X, Y, Z> {

    void accept(X x, Y y, Z z);

    default TriConsumer<X, Y, Z> andThen(TriConsumer<? super X, ? super Y, ? super Z> after) {
        Objects.requireNonNull(after);

        return (x, y, z) -> {
            accept(x, y, z);
            after.accept(x, y, z);
        };
    }
}