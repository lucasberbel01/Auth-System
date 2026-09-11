package com.lucasberbel01.loginsystem.service;

import com.lucasberbel01.loginsystem.dto.UserResponseDTO;
import com.lucasberbel01.loginsystem.exception.UserNotFoundException;
import com.lucasberbel01.loginsystem.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {


    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }


    //FINDBY
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAll() {

        List<UserResponseDTO> users = repo.findAll().stream().
                map(UserResponseDTO::fromEntity)
                .toList();

        if (users.isEmpty()) {
            throw new UserNotFoundException("Users not found");
        }

        return users;
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id){
        return repo.findById(id)
                .map(UserResponseDTO::fromEntity)
                .orElseThrow(() -> new UserNotFoundException("User not found"));


    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserByUsername(String username){
        return repo.findUserByUsername(username)
                .map(UserResponseDTO::fromEntity)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserByEmail(String email){
        return repo.findUserByEmail(email)
                .map(UserResponseDTO::fromEntity)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

    }


}
