package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class JustifiableLine implements StringCombiner {
    private final int desiredWidth;

    private final List<String> words = new ArrayList<>();
    private int totalSymbolsCount = 0;

    JustifiableLine(int desiredWidth) {
        this.desiredWidth = desiredWidth;
    }

    private int getRemainingSpaceLength() {
        return desiredWidth - totalSymbolsCount;
    }

    public int getDesiredWidth() {
        return desiredWidth;
    }

    @Override
    public boolean canAdd(String word) {
        var newTotalSymbolsCount = totalSymbolsCount + word.length();
        var minimalDelimitersCount = words.size();
        return desiredWidth - newTotalSymbolsCount >= minimalDelimitersCount;
    }

    @Override
    public boolean tryAdd(String word) {
        if (word.length() > desiredWidth) {
            throw new IllegalArgumentException("");
        }

        if (!canAdd(word)) {
            return false;
        }

        totalSymbolsCount += word.length();
        words.add(word);
        return true;
    }

    @Override
    public void clear() {
        words.clear();
        totalSymbolsCount = 0;
    }

    private String justifySingleWord(String delimiter) {
        return words.get(0) + delimiter.repeat(getRemainingSpaceLength());
    }

    String justify(String delimiter) {
        if (words.size() == 1) {
            return justifySingleWord(delimiter);
        }

        var delimitersCount = words.size() - 1;
        var delimiterLength = getRemainingSpaceLength() / delimitersCount;
        var residueDelimiterSize = getRemainingSpaceLength() % delimitersCount;

        var builder = new StringBuilder();
        builder.append(words.get(0));

        IntStream.range(1, words.size()).forEach(wordNumber -> {
            var currentDelimiterLength = wordNumber <= residueDelimiterSize
                    ? delimiterLength + 1
                    : delimiterLength;
            builder.append(delimiter.repeat(currentDelimiterLength));
            builder.append(words.get(wordNumber));
        });

        return builder.toString();
    }

    @Override
    public String compile() {
        return justify(" ");
    }

    @Override
    public Stream<String> stream() {
        return words.stream();
    }
}
