package com.github.DNeberize.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.github.DNeberize.demo.domain.WordleGame;

public interface WordleGameRepository extends JpaRepository<WordleGame, Long> {

    @EntityGraph(attributePaths = { "secretWord", "guesses" })
    Optional<WordleGame> findFirstByPlayerIdOrderByCreatedAtDesc(Long playerId);
}