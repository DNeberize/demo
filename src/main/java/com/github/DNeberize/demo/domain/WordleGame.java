package com.github.DNeberize.demo.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "wordle_game")
public class WordleGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private AppUser player;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "secret_word_id", nullable = false)
    private WordEntry secretWord;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private GameStatus status;

    @Column(nullable = false)
    private int attemptLimit;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime finishedAt;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("guessIndex asc")
    private List<WordleGuess> guesses = new ArrayList<>();

    protected WordleGame() {
    }

    public WordleGame(AppUser player, WordEntry secretWord) {
        this.player = player;
        this.secretWord = secretWord;
        this.status = GameStatus.IN_PROGRESS;
        this.attemptLimit = 6;
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = GameStatus.IN_PROGRESS;
        }
        if (attemptLimit == 0) {
            attemptLimit = 6;
        }
    }

    public void addGuess(WordleGuess guess) {
        guess.attachToGame(this);
        guesses.add(guess);
    }

    public void markWon() {
        status = GameStatus.WON;
        finishedAt = LocalDateTime.now();
    }

    public void markLost() {
        status = GameStatus.LOST;
        finishedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public AppUser getPlayer() {
        return player;
    }

    public WordEntry getSecretWord() {
        return secretWord;
    }

    public GameStatus getStatus() {
        return status;
    }

    public int getAttemptLimit() {
        return attemptLimit;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public List<WordleGuess> getGuesses() {
        return guesses;
    }

    public int getAttemptsUsed() {
        return guesses.size();
    }

    public int getAttemptsRemaining() {
        return Math.max(0, attemptLimit - guesses.size());
    }

    public boolean getFinished() {
        return status != GameStatus.IN_PROGRESS;
    }

    public boolean getWon() {
        return status == GameStatus.WON;
    }
}