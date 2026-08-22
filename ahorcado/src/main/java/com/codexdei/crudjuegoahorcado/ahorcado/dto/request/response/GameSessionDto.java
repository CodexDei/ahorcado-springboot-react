package com.codexdei.crudjuegoahorcado.ahorcado.dto.request.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.codexdei.crudjuegoahorcado.ahorcado.enums.Difficulty;

public class GameSessionDto {

    private UUID id;

    private Long userId;

    private String originalWord;

    private char[] maskedWord;

    private Difficulty currentDifficulty;

    private Integer attemptsLeft;

    private LocalDateTime startTime;

    private Long totalScore = 0L;

    public GameSessionDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOriginalWord() {
        return originalWord;
    }

    public void setOriginalWord(String originalWord) {
        this.originalWord = originalWord;
    }

    public char[] getMaskedWord() {
        return maskedWord;
    }

    public void setMaskedWord(char[] maskedWord) {
        this.maskedWord = maskedWord;
    }

    public Difficulty getCurrentDifficulty() {
        return currentDifficulty;
    }

    public void setCurrentDifficulty(Difficulty currentDifficulty) {
        this.currentDifficulty = currentDifficulty;
    }

    public Integer getAttemptsLeft() {
        return attemptsLeft;
    }

    public void setAttemptsLeft(Integer attemptsLeft) {
        this.attemptsLeft = attemptsLeft;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Long totalScore) {
        this.totalScore = totalScore;
    }
}