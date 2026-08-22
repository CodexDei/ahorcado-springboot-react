package com.codexdei.crudjuegoahorcado.ahorcado.dto.request.response.response;

import java.time.LocalDateTime;
import java.util.Date;

import com.codexdei.crudjuegoahorcado.ahorcado.enums.Difficulty;

public class ScoreBoardResponseDTo {

    private String username;

    private Long score;

    private Difficulty maxLevel;

    private LocalDateTime date;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Difficulty getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(Difficulty maxLevel) {
        this.maxLevel = maxLevel;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
