package com.example.familybudget.service;

import com.example.familybudget.entity.UserEntity;
import com.example.familybudget.exception.UserAlreadyExistException;
import com.example.familybudget.exception.UserNotFoundException;

import java.util.List;

public interface UserService {

    List<UserEntity> getAll();
    Long getIdByEmail(String email) throws UserNotFoundException;
    UserEntity getById(Long id) throws UserNotFoundException;
    UserEntity getByEmail(String email) throws UserNotFoundException;
    UserEntity update(UserEntity user, Long id) throws UserAlreadyExistException, UserNotFoundException;
    UserEntity save(UserEntity user) throws UserAlreadyExistException;
    boolean activateUser(String code);
    UserEntity delete(Long id) throws UserNotFoundException, UserAlreadyExistException;
}
