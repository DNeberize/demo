package com.github.DNeberize.demo.service;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import com.github.DNeberize.demo.domain.AnswerWord;
import com.github.DNeberize.demo.domain.WordEntry;
import com.github.DNeberize.demo.repository.AnswerWordRepository;
import com.github.DNeberize.demo.repository.WordEntryRepository;

@Service
public class WordCatalogSeeder implements ApplicationRunner {

    private final AnswerWordRepository answerWordRepository;
    private final WordEntryRepository wordEntryRepository;
    private final WordNormalizer wordNormalizer;

    public WordCatalogSeeder(AnswerWordRepository answerWordRepository, WordEntryRepository wordEntryRepository,
            WordNormalizer wordNormalizer) {
        this.answerWordRepository = answerWordRepository;
        this.wordEntryRepository = wordEntryRepository;
        this.wordNormalizer = wordNormalizer;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        List<String> dictionaryWords = loadWords("words.json");
        if (dictionaryWords.isEmpty()) {
            throw new IllegalStateException("No valid 5-letter words were found in words.json.");
        }

        List<String> answerWords = loadWords("words_toGet.json");
        if (answerWords.isEmpty()) {
            throw new IllegalStateException("No valid 5-letter words were found in words_toGet.json.");
        }

        Map<String, WordEntry> entriesByValue = wordEntryRepository.findAll().stream()
                .collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getValue(), entry), Map::putAll);

        dictionaryWords.forEach(word -> entriesByValue.computeIfAbsent(word, WordEntry::new));
        answerWords.forEach(word -> entriesByValue.computeIfAbsent(word, WordEntry::new));

        List<WordEntry> synchronizedEntries = wordEntryRepository.saveAll(entriesByValue.values());
        Map<String, WordEntry> persistedEntries = synchronizedEntries.stream()
                .collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getValue(), entry), Map::putAll);

        answerWordRepository.deleteAllInBatch();
        List<AnswerWord> answerEntries = answerWords.stream()
                .map(persistedEntries::get)
                .filter(java.util.Objects::nonNull)
                .map(AnswerWord::new)
                .toList();

        answerWordRepository.saveAll(answerEntries);
    }

    private List<String> loadWords(String fileName) throws Exception {
        Resource resource = resolveWordsResource(fileName);
        String json = FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
        Map<String, Object> payload = JsonParserFactory.getJsonParser().parseMap(json);
        Object rawWords = payload.get("words");
        if (!(rawWords instanceof List<?> values)) {
            throw new IllegalStateException(fileName + " is missing the words array.");
        }

        return values.stream()
                .map(Object::toString)
                .map(this::safeNormalize)
                .flatMap(Optional::stream)
                .distinct()
                .sorted()
                .toList();
    }

    private Resource resolveWordsResource(String fileName) {
        ClassPathResource classPathResource = new ClassPathResource(fileName);
        if (classPathResource.exists()) {
            return classPathResource;
        }

        Path filePath = Path.of(fileName);
        if (Files.exists(filePath)) {
            return new FileSystemResource(filePath);
        }

        throw new IllegalStateException(fileName + " was not found on the classpath or in the project root.");
    }

    private Optional<String> safeNormalize(String value) {
        try {
            return Optional.of(wordNormalizer.normalizeDictionaryWord(value));
        }
        catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }
}