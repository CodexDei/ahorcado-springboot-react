package com.codexdei.crudjuegoahorcado.ahorcado.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.codexdei.crudjuegoahorcado.ahorcado.entities.User;

public interface userRepository extends CrudRepository<User,Long> {

    boolean existBy(String username);
    Optional<User> findByUsername(String username);

}
