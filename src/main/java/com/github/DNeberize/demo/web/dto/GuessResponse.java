package com.github.DNeberize.demo.web.dto;

import java.util.List;

public record GuessResponse(String guess, List<Integer> result, int attemptsUsed, int attemptsRemaining, boolean solved,
        boolean finished, int gamesPlayed, int wins, int currentStreak, int bestStreak, String winRateLabel) {
}