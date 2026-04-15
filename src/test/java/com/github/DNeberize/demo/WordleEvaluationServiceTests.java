package com.github.DNeberize.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.github.DNeberize.demo.service.WordleEvaluationService;

class WordleEvaluationServiceTests {

	private final WordleEvaluationService wordleEvaluationService = new WordleEvaluationService();

	@Test
	void evaluatesDuplicateLettersLikeWordle() {
		int[] result = wordleEvaluationService.evaluate("allee", "eagle");

		assertThat(result).containsExactly(2, 2, 0, 2, 1);
	}
}