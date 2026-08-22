package com.codexdei.crudjuegoahorcado.ahorcado.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codexdei.crudjuegoahorcado.ahorcado.dto.request.response.response.WordReponseDto;
import com.codexdei.crudjuegoahorcado.ahorcado.entities.Word;
import com.codexdei.crudjuegoahorcado.ahorcado.enums.Difficulty;
import com.codexdei.crudjuegoahorcado.ahorcado.services.WordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/words")
public class WordController {

    private final WordService wordService;

    public WordController(WordService wordService) {

        this.wordService = wordService;
    }

    @GetMapping("/random")
    public WordReponseDto getRandomWord(
            @RequestParam Difficulty difficulty) {

        Word word = wordService.getRandomWord(difficulty);

        return new WordReponseDto(
                word.getWord());
    }

}
