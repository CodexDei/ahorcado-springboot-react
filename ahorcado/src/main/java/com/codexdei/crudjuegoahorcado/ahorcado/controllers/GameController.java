package com.codexdei.crudjuegoahorcado.ahorcado.controllers;

import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import com.codexdei.crudjuegoahorcado.ahorcado.dto.request.response.request.GuessRequestDto;
import com.codexdei.crudjuegoahorcado.ahorcado.dto.request.response.response.GameWordResponseDto;
import com.codexdei.crudjuegoahorcado.ahorcado.services.GameService;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {

        this.gameService = gameService;
    }

    @PostMapping("/start")
    public GameWordResponseDto startGame(
            @RequestParam Long userId) {

        return gameService.startGame(userId);
    }

    @PostMapping("/guess/{gameId}")
    public GameWordResponseDto guessLetter(

            @PathVariable UUID gameId,

            @RequestBody GuessRequestDto request) {

        return gameService.guessLetter(
                gameId,
                request);
    }
}