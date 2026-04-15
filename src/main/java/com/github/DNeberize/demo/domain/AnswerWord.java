package com.github.DNeberize.demo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "answer_word")
public class AnswerWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "word_entry_id", nullable = false, unique = true)
    private WordEntry wordEntry;

    protected AnswerWord() {
    }

    public AnswerWord(WordEntry wordEntry) {
        this.wordEntry = wordEntry;
    }

    public Long getId() {
        return id;
    }

    public WordEntry getWordEntry() {
        return wordEntry;
    }
}