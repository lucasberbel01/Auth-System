package com.lucasberbel01.loginsystem.service;

import com.lucasberbel01.loginsystem.dto.UserRequestDTO;
import com.lucasberbel01.loginsystem.dto.UserResponseDTO;
import com.lucasberbel01.loginsystem.exception.UserNotFoundException;
import com.lucasberbel01.loginsystem.model.User;
import com.lucasberbel01.loginsystem.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class UserService {


    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;



    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }


    //FINDBY
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getAll(Pageable pageable) {

        return repo.findAll(pageable).map(UserResponseDTO::fromEntity);

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
    //---------------------------------------------------------------------------------------------------------------------
    //SAVE
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO request){

        User user = new User();

        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());

        User savedUser = repo.save(user);

        return UserResponseDTO.fromEntity(savedUser);


    }


}
