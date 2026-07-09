package com.codexdei.crudjuegoahorcado.ahorcado.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.codexdei.crudjuegoahorcado.ahorcado.entities.Word;
import java.util.List;


public interface wordRepository extends CrudRepository<Word,Long>{

    Optional<Word> findById(Long id);

}
