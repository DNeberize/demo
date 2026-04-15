package com.github.DNeberize.demo.web.dto;

import java.util.List;

public class GuessRowView {

    private final String guess;
    private final List<Integer> result;

    public GuessRowView(String guess, List<Integer> result) {
        this.guess = guess;
        this.result = result;
    }

    public String getGuess() {
        return guess;
    }

    public List<Integer> getResult() {
        return result;
    }
}