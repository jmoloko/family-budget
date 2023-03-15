package com.example.familybudget.rest.v1;

import com.example.familybudget.dto.CredentialRequestDTO;
import com.example.familybudget.dto.JsonViews;
import com.example.familybudget.dto.UserDTO;
import com.example.familybudget.entity.Role;
import com.example.familybudget.entity.UserEntity;
import com.example.familybudget.exception.UserAlreadyExistException;
import com.example.familybudget.exception.UserNotFoundException;
import com.example.familybudget.service.UserService;
import com.example.familybudget.utils.Utils;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/admin")
public class AdminRestController {

    private final UserService userService;
    private final Utils utils;

    @GetMapping("/users")
    @JsonView(JsonViews.FullView.class)
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAll()
                .stream().map(UserDTO::toDto)
                .collect(Collectors.toList()));
    }

    @PostMapping("/users")
    @PreAuthorize("hasAuthority('level:high')")
    public ResponseEntity<?> saveNewUser(@RequestBody CredentialRequestDTO newUser) {
        return utils.saveNewUser(newUser);
    }

    @GetMapping("/users/{id}")
    @JsonView(JsonViews.FullView.class)
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(UserDTO.toDto(userService.getById(id)), OK);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateStatusForUserById(@PathVariable Long id,
                                                     @RequestBody UserEntity userEntity) {

        try {
            UserEntity currentUser = userService.getById(id);

            if (currentUser.getRole() == Role.MODERATOR || currentUser.getRole() == Role.ADMIN) {
                return new ResponseEntity<>("Not enough rights", HttpStatus.METHOD_NOT_ALLOWED);
            }

            currentUser.setStatus(userEntity.getStatus());
            return new ResponseEntity<>(UserDTO.toDto(userService.update(currentUser, id)), OK);
        } catch (UserNotFoundException | UserAlreadyExistException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/users/{id}")
    @PreAuthorize("hasAuthority('level:high')")
    public ResponseEntity<?> updateUserById(@PathVariable Long id,
                                            @RequestBody UserEntity userEntity) {

        try {
            UserEntity currentUser = userService.getById(id);

            if (currentUser.getRole() == Role.ADMIN) {
                return new ResponseEntity<>("Not enough rights", HttpStatus.METHOD_NOT_ALLOWED);
            }

            currentUser.setEmail(userEntity.getEmail());
            currentUser.setName(userEntity.getName());
            currentUser.setRole(userEntity.getRole());
            currentUser.setStatus(userEntity.getStatus());

            return new ResponseEntity<>(UserDTO.toDto(userService.update(currentUser, id)), OK);
        } catch (UserNotFoundException | UserAlreadyExistException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('level:high')")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {

        try {
//            UserEntity currentUser = userService.getById(id);

            return new ResponseEntity<>(UserDTO.toDto(userService.delete(id)), OK);
        } catch (UserNotFoundException | UserAlreadyExistException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
