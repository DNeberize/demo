package com.github.DNeberize.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DictionaryRefreshScheduler {

    private static final Logger log = LoggerFactory.getLogger(DictionaryRefreshScheduler.class);

    private final DictionaryStatsService dictionaryStatsService;

    public DictionaryRefreshScheduler(DictionaryStatsService dictionaryStatsService) {
        this.dictionaryStatsService = dictionaryStatsService;
    }

    @Scheduled(fixedDelay = 60000)
    public void evictDictionaryCache() {
        dictionaryStatsService.clearDictionaryCache();
        log.info("Cleared dictionarySize cache");
    }
}
