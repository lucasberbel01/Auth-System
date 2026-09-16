package com.lucasberbel01.loginsystem.service;

import com.lucasberbel01.loginsystem.dto.LoginRequestDTO;
import com.lucasberbel01.loginsystem.dto.UserPatchDTO;
import com.lucasberbel01.loginsystem.dto.UserRequestDTO;
import com.lucasberbel01.loginsystem.dto.UserResponseDTO;
import com.lucasberbel01.loginsystem.enums.UserRole;
import com.lucasberbel01.loginsystem.exception.EmailAlreadyTakenException;
import com.lucasberbel01.loginsystem.exception.EmailOrPasswordIncorrectException;
import com.lucasberbel01.loginsystem.exception.UserNotFoundException;
import com.lucasberbel01.loginsystem.exception.UsernameAlreadyTakenException;
import com.lucasberbel01.loginsystem.model.User;
import com.lucasberbel01.loginsystem.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {


    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    //---------------------------------------------------------------------------------------------------------------------
    //FINDBY
    //---------------------------------------------------------------------------------------------------------------------

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
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
    //---------------------------------------------------------------------------------------------------------------------

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO request){

        User user = new User();

        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(UserRole.ROLE_USER);
        User savedUser = repo.save(user);

        return UserResponseDTO.fromEntity(savedUser);
    }

    //---------------------------------------------------------------------------------------------------------------------
    //PUT E PATCH
    //---------------------------------------------------------------------------------------------------------------------

   @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO request){ //metodo UPDATE de usuario  sem alterar a ROLE
        User user = repo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        validateUsernameNotTaken(request.username(), id); //valida se o usuario e email ja estao em uso
        validateEmailNotTaken(request.email(), id);

        user.setUsername(request.username());
        user.setEmail(request.email());

        if (request.password() != null  &&  !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        User updatedUser = repo.save(user);

        return UserResponseDTO.fromEntity(updatedUser);
   }

   @Transactional
    public UserResponseDTO updateUserRole(Long id, UserRole newRole){ // metodo ADMIN ONLY para a troca de role do usuario
        User user = repo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

       if (newRole == null) {
           throw new IllegalArgumentException("Role cannot be null");
       }else if (newRole == user.getRole()){
           return UserResponseDTO.fromEntity(user);
       }

        user.setRole(newRole);

        User updatedUser = repo.save(user);

        return UserResponseDTO.fromEntity(updatedUser);
   }

   @Transactional
   public UserResponseDTO patchUser(Long id, UserPatchDTO request){
        User user = repo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if(request.username() != null &&  !request.username().isBlank()){
            validateUsernameNotTaken(request.username(), id);
            user.setUsername(request.username());
        }

        if(request.email() != null &&  !request.email().isBlank()){
            validateEmailNotTaken(request.email(), id);
            user.setEmail(request.email());
        }

        if(request.password() != null &&  !request.password().isBlank()){
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        User patchUser = repo.save(user);

        return UserResponseDTO.fromEntity(patchUser);
   }

    //---------------------------------------------------------------------------------------------------------------------
    //DELETE
    //---------------------------------------------------------------------------------------------------------------------

    @Transactional
    public void deleteUser(Long id){
        User user = repo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        repo.deleteById(id);
    }

    //================================================================================
    //LOGIN
    // ================================================================================
    @Transactional(readOnly = true)
    public UserResponseDTO login(LoginRequestDTO request){
        User user = repo.findUserByEmail(request.email()).orElseThrow(() -> new EmailOrPasswordIncorrectException("Wrong email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new EmailOrPasswordIncorrectException("Wrong email or password");
        }

        return UserResponseDTO.fromEntity(user);
    }


    //---------------------------------------------------------------------------------------------------------------------
    //VALIDATIONS
    //---------------------------------------------------------------------------------------------------------------------

    private void validateUsernameNotTaken(String username, Long currentUserId) { //metodo para verificar se o nome ja esta em uso
        repo.findUserByUsername(username) // retorna um optional
                .filter(existing -> !existing.getId().equals(currentUserId)) //verifica se o id do usuario encontrado pelo nome é o mesmo do que quer ser atualizado
                .ifPresent(existing -> { //se nao for o mesmo, joga exception
                    throw new UsernameAlreadyTakenException("Username already in use");
                });
    }

    private void validateEmailNotTaken(String email, Long currentUserId) {
        repo.findUserByEmail(email)
                .filter(existing -> !existing.getId().equals(currentUserId))
                .ifPresent(existing -> {
                    throw new EmailAlreadyTakenException("Email already in use");
                });
    }

}
