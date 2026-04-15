package com.github.DNeberize.demo.web.dto;

import java.util.List;

public class HomeView {

    private final Long gameId;
    private final List<GuessRowView> guesses;
    private final int maxAttempts;
    private final int attemptsRemaining;
    private final boolean finished;
    private final boolean won;
    private final String revealedWord;

    public HomeView(Long gameId, List<GuessRowView> guesses, int maxAttempts, int attemptsRemaining, boolean finished,
            boolean won, String revealedWord) {
        this.gameId = gameId;
        this.guesses = guesses;
        this.maxAttempts = maxAttempts;
        this.attemptsRemaining = attemptsRemaining;
        this.finished = finished;
        this.won = won;
        this.revealedWord = revealedWord;
    }

    public Long getGameId() {
        return gameId;
    }

    public List<GuessRowView> getGuesses() {
        return guesses;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public int getAttemptsRemaining() {
        return attemptsRemaining;
    }

    public boolean getFinished() {
        return finished;
    }

    public boolean getWon() {
        return won;
    }

    public String getRevealedWord() {
        return revealedWord;
    }
}