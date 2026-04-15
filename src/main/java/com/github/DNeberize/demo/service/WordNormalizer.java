package com.github.DNeberize.demo.service;

import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class WordNormalizer {

    private static final Pattern WORD_PATTERN = Pattern.compile("^\\p{L}{5}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[\\p{L}0-9]{3,24}$");

    public String normalizeDictionaryWord(String value) {
        String normalized = normalize(value);
        if (normalized.codePointCount(0, normalized.length()) != 5 || !WORD_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Word must be exactly 5 letters.");
        }
        return normalized;
    }

    public String normalizeGuess(String value) {
        try {
            return normalizeDictionaryWord(value);
        }
        catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Enter a valid 5-letter word with letters only.");
        }
    }

    public String normalizeUsername(String value) {
        String normalized = normalize(value);
        if (!USERNAME_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Choose a player name using 3 to 24 letters or numbers.");
        }
        return normalized;
    }

    private String normalize(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Value is required.");
        }
        String normalized = value.trim().toLowerCase(Locale.ROOT);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("Value is required.");
        }
        return normalized;
    }
}