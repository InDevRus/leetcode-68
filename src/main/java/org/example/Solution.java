package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class Solution {
    public Stream<String> justifyIterablely(
            Stream<String> words,
            int desiredWidth,
            String delimiter) {
        var connector = new JustifiableLine(desiredWidth);

        Function<String, String> mapWordsToLine = word -> {
            if (connector.tryAdd(word)) {
                return null;
            }

            var connectedLine = connector.justify(delimiter);
            connector.clear();
            connector.tryAdd(word);
            return connectedLine;
        };

        Supplier<String> connectLastLine = () -> {
            var lastLine = connector.stream().collect(Collectors.joining(delimiter));
            var padding = delimiter.repeat(desiredWidth - lastLine.length());
            return lastLine + padding;
        };

        var streamUntilLast = words.map(mapWordsToLine).filter(Objects::nonNull);
        var streamOfLast = Stream.generate(connectLastLine).limit(1);
        return Stream.concat(streamUntilLast, streamOfLast);
    }

    public List<String> fullJustify(String[] words, int desiredWidth) {
        return justifyIterablely(Arrays.stream(words), desiredWidth, " ").toList();
    }
}
