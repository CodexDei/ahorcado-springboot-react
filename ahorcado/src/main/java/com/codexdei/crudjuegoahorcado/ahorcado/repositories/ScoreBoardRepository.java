package com.codexdei.crudjuegoahorcado.ahorcado.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.codexdei.crudjuegoahorcado.ahorcado.entities.ScoreBoard;

public interface ScoreBoardRepository extends CrudRepository<ScoreBoard, Long> {

    List<ScoreBoard> findScoreBoards();

}
