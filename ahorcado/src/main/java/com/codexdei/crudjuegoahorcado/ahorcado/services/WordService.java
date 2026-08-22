package com.codexdei.crudjuegoahorcado.ahorcado.services;

import com.codexdei.crudjuegoahorcado.ahorcado.entities.Word;
import com.codexdei.crudjuegoahorcado.ahorcado.enums.Difficulty;

public interface WordService {

    Word getRandomWord(Difficulty difficulty);

}
