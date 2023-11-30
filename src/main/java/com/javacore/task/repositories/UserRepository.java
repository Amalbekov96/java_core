package com.javacore.task.repositories;

import com.javacore.task.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    void deleteById(Long id);

    List<User> findAll();

    Optional<User> findByUsername(String username);
}
