package com.lucasberbel01.loginsystem.repository;

import com.lucasberbel01.loginsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);
}
