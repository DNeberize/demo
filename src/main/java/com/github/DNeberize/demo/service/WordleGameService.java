package com.github.DNeberize.demo.service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.DNeberize.demo.domain.AppUser;
import com.github.DNeberize.demo.domain.AnswerWord;
import com.github.DNeberize.demo.domain.GameStatus;
import com.github.DNeberize.demo.domain.WordEntry;
import com.github.DNeberize.demo.domain.WordleGame;
import com.github.DNeberize.demo.domain.WordleGuess;
import com.github.DNeberize.demo.repository.AppUserRepository;
import com.github.DNeberize.demo.repository.AnswerWordRepository;
import com.github.DNeberize.demo.repository.WordEntryRepository;
import com.github.DNeberize.demo.repository.WordleGameRepository;
import com.github.DNeberize.demo.web.dto.GuessResponse;
import com.github.DNeberize.demo.web.dto.GuessRowView;
import com.github.DNeberize.demo.web.dto.HomeView;

@Service
public class WordleGameService {

    private final WordleGameRepository wordleGameRepository;
    private final AnswerWordRepository answerWordRepository;
    private final WordEntryRepository wordEntryRepository;
    private final AppUserRepository appUserRepository;
    private final WordNormalizer wordNormalizer;
    private final WordleEvaluationService wordleEvaluationService;

    public WordleGameService(WordleGameRepository wordleGameRepository, AnswerWordRepository answerWordRepository,
            WordEntryRepository wordEntryRepository, AppUserRepository appUserRepository,
            WordNormalizer wordNormalizer, WordleEvaluationService wordleEvaluationService) {
        this.wordleGameRepository = wordleGameRepository;
        this.answerWordRepository = answerWordRepository;
        this.wordEntryRepository = wordEntryRepository;
        this.appUserRepository = appUserRepository;
        this.wordNormalizer = wordNormalizer;
        this.wordleEvaluationService = wordleEvaluationService;
    }

    @Transactional
    public HomeView buildHomeView(long userId) {
        WordleGame game = wordleGameRepository.findFirstByPlayerIdOrderByCreatedAtDesc(userId)
                .orElseGet(() -> createGame(userId));

        List<GuessRowView> rows = game.getGuesses().stream()
                .map(guess -> new GuessRowView(guess.getGuessWord(), guess.getScoreValues()))
                .toList();

        String revealedWord = game.getFinished() ? game.getSecretWord().getValue() : null;
        return new HomeView(game.getId(), rows, game.getAttemptLimit(), game.getAttemptsRemaining(), game.getFinished(),
                game.getWon(), revealedWord);
    }

    @Transactional
    public void startNewGame(long userId) {
        WordleGame latestGame = wordleGameRepository.findFirstByPlayerIdOrderByCreatedAtDesc(userId).orElse(null);
        if (latestGame != null && latestGame.getStatus() == GameStatus.IN_PROGRESS) {
            throw new IllegalStateException("Finish the current round before starting a new one.");
        }
        createGame(userId);
    }

    @Transactional
    public GuessResponse submitGuess(long userId, String rawGuess) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Player was not found."));
        WordleGame game = wordleGameRepository.findFirstByPlayerIdOrderByCreatedAtDesc(userId)
                .orElseGet(() -> createGame(userId));

        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            throw new IllegalStateException("This round is finished. Start a new round to keep playing.");
        }

        String guess = wordNormalizer.normalizeGuess(rawGuess);
        wordEntryRepository.findByValue(guess)
                .orElseThrow(() -> new IllegalArgumentException("That guess is not in the dictionary."));

        int[] result = wordleEvaluationService.evaluate(guess, game.getSecretWord().getValue());
        WordleGuess guessEntry = new WordleGuess(game.getAttemptsUsed() + 1, guess, toPattern(result));
        game.addGuess(guessEntry);

        boolean solved = Arrays.stream(result).allMatch(code -> code == 1);
        if (solved) {
            game.markWon();
            user.recordWin(game.getAttemptsUsed());
        }
        else if (game.getAttemptsUsed() >= game.getAttemptLimit()) {
            game.markLost();
            user.recordLoss();
        }

        List<Integer> resultValues = Arrays.stream(result).boxed().toList();
        return new GuessResponse(guess, resultValues, game.getAttemptsUsed(), game.getAttemptsRemaining(), solved,
                game.getFinished(), user.getGamesPlayed(), user.getWins(), user.getCurrentStreak(),
                user.getBestStreak(), user.getWinRateLabel());
    }

    @Transactional(readOnly = true)
    public long getDictionarySize() {
        return wordEntryRepository.count();
    }

    private WordleGame createGame(long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Player was not found."));
        WordEntry secretWord = pickRandomWord();
        return wordleGameRepository.save(new WordleGame(user, secretWord));
    }

    private WordEntry pickRandomWord() {
        long total = answerWordRepository.count();
        if (total == 0) {
            throw new IllegalStateException("Answer pool is empty.");
        }
        int offset = ThreadLocalRandom.current().nextInt(Math.toIntExact(total));
        return answerWordRepository.findAll(PageRequest.of(offset, 1, Sort.by("id").ascending()))
                .stream()
                .findFirst()
                .map(AnswerWord::getWordEntry)
                .orElseThrow(() -> new IllegalStateException("Could not load a secret word."));
    }

    private String toPattern(int[] result) {
        StringBuilder builder = new StringBuilder(result.length);
        for (int value : result) {
            builder.append(value);
        }
        return builder.toString();
    }
}