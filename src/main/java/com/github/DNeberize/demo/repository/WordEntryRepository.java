package com.github.DNeberize.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.DNeberize.demo.domain.WordEntry;

public interface WordEntryRepository extends JpaRepository<WordEntry, Long> {

    Optional<WordEntry> findByValue(String value);
}