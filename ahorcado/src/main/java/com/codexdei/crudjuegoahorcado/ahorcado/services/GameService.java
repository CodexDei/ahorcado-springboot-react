package com.codexdei.crudjuegoahorcado.ahorcado.services;

import com.codexdei.crudjuegoahorcado.ahorcado.dto.request.response.request.GuessRequestDto;
import com.codexdei.crudjuegoahorcado.ahorcado.dto.request.response.response.GameWordResponseDto;

import java.util.UUID;

public interface GameService {

        GameWordResponseDto startGame(Long userId);

        GameWordResponseDto guessLetter(
                        UUID gameId,
                        GuessRequestDto request);
}
