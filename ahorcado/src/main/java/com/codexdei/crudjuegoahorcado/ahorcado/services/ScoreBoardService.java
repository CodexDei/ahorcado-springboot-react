package com.codexdei.crudjuegoahorcado.ahorcado.services;

import java.util.List;

import com.codexdei.crudjuegoahorcado.ahorcado.entities.ScoreBoard;


public interface ScoreBoardService {

    List<ScoreBoard> getTop20Scores(); 

}
