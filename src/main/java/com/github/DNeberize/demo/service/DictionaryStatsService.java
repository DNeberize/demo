package com.github.DNeberize.demo.service;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DictionaryStatsService {

    private final WordleGameService wordleGameService;
    private final CacheManager cacheManager;

    public DictionaryStatsService(WordleGameService wordleGameService, CacheManager cacheManager) {
        this.wordleGameService = wordleGameService;
        this.cacheManager = cacheManager;
    }

    @Cacheable("dictionarySize")
    public long getCachedDictionarySize() {
        return wordleGameService.getDictionarySize();
    }

    @CacheEvict(value = "dictionarySize", allEntries = true)
    public void clearDictionaryCache() {
        if (cacheManager.getCache("dictionarySize") != null) {
            cacheManager.getCache("dictionarySize").clear();
        }
    }
}
