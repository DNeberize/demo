package com.github.DNeberize.demo.domain;

import java.time.LocalDateTime;
import java.util.Locale;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required.")
    @Size(min = 3, max = 24, message = "Username must be 3 to 24 characters.")
    @Column(nullable = false, unique = true, length = 24)
    private String username;

    @Column(nullable = false)
    private int gamesPlayed;

    @Column(nullable = false)
    private int wins;

    @Column(nullable = false)
    private int currentStreak;

    @Column(nullable = false)
    private int bestStreak;

    @Column(nullable = false)
    private int totalWinningGuesses;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected AppUser() {
    }

    public AppUser(String username) {
        this.username = username;
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void recordWin(int guessCount) {
        gamesPlayed++;
        wins++;
        currentStreak++;
        bestStreak = Math.max(bestStreak, currentStreak);
        totalWinningGuesses += guessCount;
    }

    public void recordLoss() {
        gamesPlayed++;
        currentStreak = 0;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getWins() {
        return wins;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public int getBestStreak() {
        return bestStreak;
    }

    public int getTotalWinningGuesses() {
        return totalWinningGuesses;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public double getWinRate() {
        if (gamesPlayed == 0) {
            return 0.0;
        }
        return (wins * 100.0) / gamesPlayed;
    }

    public String getWinRateLabel() {
        return String.format(Locale.US, "%.0f%%", getWinRate());
    }

    public double getAverageWinningGuesses() {
        if (wins == 0) {
            return 0.0;
        }
        return totalWinningGuesses / (double) wins;
    }

    public String getAverageWinningGuessesLabel() {
        if (wins == 0) {
            return "-";
        }
        return String.format(Locale.US, "%.1f", getAverageWinningGuesses());
    }
}