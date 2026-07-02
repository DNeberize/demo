package com.github.DNeberize.demo.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;

class DictionaryStatsServiceTests {

    @Test
    void clearDictionaryCacheClearsTheConfiguredCache() {
        WordleGameService wordleGameService = mock(WordleGameService.class);
        CacheManager cacheManager = new TestCacheManager();
        DictionaryStatsService service = new DictionaryStatsService(wordleGameService, cacheManager);

        when(wordleGameService.getDictionarySize()).thenReturn(42L);

        service.getCachedDictionarySize();
        assertThatCode(() -> service.clearDictionaryCache()).doesNotThrowAnyException();

        verify(wordleGameService).getDictionarySize();
    }

    private static class TestCacheManager implements CacheManager {
        private final Cache cache = new ConcurrentMapCache("dictionarySize");

        @Override
        public Cache getCache(String name) {
            return "dictionarySize".equals(name) ? cache : null;
        }

        @Override
        public Collection<String> getCacheNames() {
            return java.util.List.of("dictionarySize");
        }
    }
}
