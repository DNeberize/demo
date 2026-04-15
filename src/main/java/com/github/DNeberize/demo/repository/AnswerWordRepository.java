package com.github.DNeberize.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.DNeberize.demo.domain.AnswerWord;

public interface AnswerWordRepository extends JpaRepository<AnswerWord, Long> {
}