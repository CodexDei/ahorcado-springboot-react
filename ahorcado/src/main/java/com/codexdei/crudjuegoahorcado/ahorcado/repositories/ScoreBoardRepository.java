package com.codexdei.crudjuegoahorcado.ahorcado.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codexdei.crudjuegoahorcado.ahorcado.entities.ScoreBoard;


public interface ScoreBoardRepository extends JpaRepository<ScoreBoard, Long> {

    List<ScoreBoard> findTop20ByOrderByScoreDesc();
}
