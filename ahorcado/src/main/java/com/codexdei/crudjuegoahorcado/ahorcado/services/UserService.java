package com.codexdei.crudjuegoahorcado.ahorcado.services;

import java.util.List;

import com.codexdei.crudjuegoahorcado.ahorcado.entities.User;

public interface UserService {

    List<User> findAll();

    User save(User user);

}
