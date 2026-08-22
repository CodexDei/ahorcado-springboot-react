package com.codexdei.crudjuegoahorcado.ahorcado.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codexdei.crudjuegoahorcado.ahorcado.entities.ScoreBoard;
import com.codexdei.crudjuegoahorcado.ahorcado.repositories.ScoreBoardRepository;

@Service
public class ScoreBoardServiceImpl implements ScoreBoardService{

    private final ScoreBoardRepository scoreBoardRepository;

    public ScoreBoardServiceImpl(ScoreBoardRepository scoreBoardRepository) {
        this.scoreBoardRepository = scoreBoardRepository;
    }

    @Override
    public List<ScoreBoard> getTop20Scores() {

        return scoreBoardRepository.findTop20ByOrderByScoreDesc();
    }

   

}
