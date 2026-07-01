package com.github.DNeberize.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DictionaryRefreshScheduler {

    private static final Logger log = LoggerFactory.getLogger(DictionaryRefreshScheduler.class);

    private final CacheManager cacheManager;

    public DictionaryRefreshScheduler(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Scheduled(fixedDelay = 60000)
    public void evictDictionaryCache() {
        if (cacheManager.getCache("dictionarySize") != null) {
            cacheManager.getCache("dictionarySize").clear();
        }
        log.info("Cleared dictionarySize cache");
    }
}
