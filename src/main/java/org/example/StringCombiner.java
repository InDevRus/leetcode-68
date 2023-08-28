package org.example;

import java.util.stream.Stream;

public interface StringCombiner {
    boolean canAdd(String string);

    boolean tryAdd(String string);

    void clear();

    String compile();

    Stream<String> stream();
}
