package com.github.DNeberize.demo.domain;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "wordle_guess")
public class WordleGuess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id", nullable = false)
    private WordleGame game;

    @Column(nullable = false)
    private int guessIndex;

    @Column(nullable = false, length = 5)
    private String guessWord;

    @Column(nullable = false, length = 5)
    private String scorePattern;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected WordleGuess() {
    }

    public WordleGuess(int guessIndex, String guessWord, String scorePattern) {
        this.guessIndex = guessIndex;
        this.guessWord = guessWord;
        this.scorePattern = scorePattern;
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    void attachToGame(WordleGame game) {
        this.game = game;
    }

    public Long getId() {
        return id;
    }

    public int getGuessIndex() {
        return guessIndex;
    }

    public String getGuessWord() {
        return guessWord;
    }

    public String getScorePattern() {
        return scorePattern;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<Integer> getScoreValues() {
        return scorePattern.chars()
                .map(Character::getNumericValue)
                .boxed()
                .toList();
    }
}