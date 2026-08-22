package com.codexdei.crudjuegoahorcado.ahorcado.services;

import java.util.Random;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.codexdei.crudjuegoahorcado.ahorcado.entities.Word;
import com.codexdei.crudjuegoahorcado.ahorcado.enums.Difficulty;
import com.codexdei.crudjuegoahorcado.ahorcado.repositories.WordRepository;

@Service
public class WordServiceImpl implements WordService {

    private final WordRepository wordRepository;
    private final Random random = new Random();

    public WordServiceImpl(WordRepository wordRepository) {

        this.wordRepository = wordRepository;
    }

    @Override
    public Word getRandomWord(Difficulty difficulty) {

        long total = wordRepository.countByDifficulty(difficulty);

        if (total == 0) {

            throw new RuntimeException("No words exist for the '" + difficulty + "'");
        }

        int randomIndex = random.nextInt((int) total);

        PageRequest pageRequest = PageRequest.of(randomIndex, 1);

        Word word = wordRepository
                .findByDifficulty(difficulty, pageRequest)
                .stream()
                .findFirst()
                .orElseThrow(
                        () -> new RuntimeException("No was word found"));

        return word;
    }

}
