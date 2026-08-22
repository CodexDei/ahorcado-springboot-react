package com.codexdei.crudjuegoahorcado.ahorcado.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.codexdei.crudjuegoahorcado.ahorcado.dto.request.response.response.ScoreBoardResponseDTo;
import com.codexdei.crudjuegoahorcado.ahorcado.services.ScoreBoardService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/scores")
public class ScoreBoardController {

    private final ScoreBoardService scoreBoardService;

    public ScoreBoardController(ScoreBoardService scoreBoardService) {
        this.scoreBoardService = scoreBoardService;
    }

    @GetMapping("/top")
    public ResponseEntity<List<ScoreBoardResponseDTo>> getTop() {

        return ResponseEntity.ok(

                scoreBoardService.getTop20Scores()
                        .stream()
                        .map(scoreBoard -> {

                            ScoreBoardResponseDTo dto = new ScoreBoardResponseDTo();

                            dto.setUsername(scoreBoard.getUser().getUsername());
                            dto.setScore(scoreBoard.getScore());
                            dto.setMaxLevel(scoreBoard.getMaxLevel());
                            dto.setDate(scoreBoard.getDate());

                            return dto;

                        }).toList());
    }
}
