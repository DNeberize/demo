package com.github.DNeberize.demo.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class WordleEvaluationService {

    public int[] evaluate(String guess, String answer) {
        int[] guessChars = guess.codePoints().toArray();
        int[] answerChars = answer.codePoints().toArray();
        int[] result = new int[guessChars.length];
        Map<Integer, Integer> remaining = new HashMap<>();

        for (int index = 0; index < guessChars.length; index++) {
            if (guessChars[index] == answerChars[index]) {
                result[index] = 1;
            }
            else {
                remaining.merge(answerChars[index], 1, Integer::sum);
            }
        }

        for (int index = 0; index < guessChars.length; index++) {
            if (result[index] == 1) {
                continue;
            }
            int count = remaining.getOrDefault(guessChars[index], 0);
            if (count > 0) {
                result[index] = 2;
                remaining.put(guessChars[index], count - 1);
            }
        }

        return result;
    }
}