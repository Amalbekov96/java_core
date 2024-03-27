package com.javacore.task.repositories;

import com.javacore.task.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.username = ?1")
    Optional<User> findUserByUsername(String username);
    boolean existsByUsername(String username);

    @Modifying
    @Query("update User u set u.password = :newPassword where u.username in (select u1.username from User u1 where u1.userId = :id)")
    void changePassword(@Param("id") long id,@Param("newPassword") String newPassword);
}
