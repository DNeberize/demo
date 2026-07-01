package com.github.DNeberize.demo.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DictionaryStatsService {

    private final WordleGameService wordleGameService;

    public DictionaryStatsService(WordleGameService wordleGameService) {
        this.wordleGameService = wordleGameService;
    }

    @Cacheable("dictionarySize")
    public long getCachedDictionarySize() {
        return wordleGameService.getDictionarySize();
    }
}
