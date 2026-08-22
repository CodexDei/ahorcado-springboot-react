package com.codexdei.crudjuegoahorcado.ahorcado.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codexdei.crudjuegoahorcado.ahorcado.entities.Word;
import com.codexdei.crudjuegoahorcado.ahorcado.enums.Difficulty;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    long countByDifficulty(Difficulty difficulty);

    Page<Word> findByDifficulty(
            Difficulty difficulty,
            Pageable pageable);

}
