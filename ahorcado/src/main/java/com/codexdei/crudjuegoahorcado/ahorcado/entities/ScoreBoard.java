package com.codexdei.crudjuegoahorcado.ahorcado.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

import com.codexdei.crudjuegoahorcado.ahorcado.enums.Difficulty;;

@Entity
@Table(name = "scores")
public class ScoreBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long score;

    @Enumerated(EnumType.STRING)
    private Difficulty maxLevel;

    private LocalDateTime date;

    public ScoreBoard() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
