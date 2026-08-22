package com.codexdei.crudjuegoahorcado.ahorcado.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codexdei.crudjuegoahorcado.ahorcado.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
