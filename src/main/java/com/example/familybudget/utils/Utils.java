package com.example.familybudget.utils;

import com.example.familybudget.dto.CredentialRequestDTO;
import com.example.familybudget.dto.UserDTO;
import com.example.familybudget.entity.Role;
import com.example.familybudget.entity.Status;
import com.example.familybudget.entity.UserEntity;
import com.example.familybudget.exception.UserAlreadyExistException;
import com.example.familybudget.service.EmailService;
import com.example.familybudget.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Getter
public class Utils {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${link.to.email}")
    private String link;

    public ResponseEntity<?> saveNewUser(CredentialRequestDTO newUser) {
        String name = newUser.getName();
        String email = newUser.getEmail();
        String password = newUser.getPassword();

        String regexEmail = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        String regexName = "^[A-Za-z0-9_]*\\s*[A-Za-z0-9_]{3,29}$";
        String regexPassword = "^[A-Za-z_]*[A-Za-z0-9_]{3,29}$";

        if (!email.matches(regexEmail)) {
            return  new ResponseEntity<>("Invalid email", HttpStatus.FORBIDDEN);
        }
        if (!name.matches(regexName)) {
            return  new ResponseEntity<>("Invalid name", HttpStatus.FORBIDDEN);
        }
        if (!password.matches(regexPassword)) {
            return  new ResponseEntity<>("Invalid password", HttpStatus.FORBIDDEN);
        }

        UserEntity nUser = new UserEntity();
        nUser.setEmail(email);
        nUser.setName(name);
        nUser.setPassword(passwordEncoder.encode(password));
        nUser.setRole(Role.USER);
        nUser.setActivationCode(UUID.randomUUID().toString());

        UserEntity savedUser;
        try {
            savedUser = userService.save(nUser);
            if (!StringUtils.isEmpty(savedUser.getEmail())){
                String message = String.format(
                        "Hello, %s! \n" +
                                "Welcome to Family Budget. Please, visit next link: %s/%s",
                        savedUser.getEmail(), link, savedUser.getActivationCode()

                );

                emailService.send(savedUser.getEmail(), "Activation Code", message);
            }

        } catch (UserAlreadyExistException e) {
            e.printStackTrace();
            return new ResponseEntity<>("User already exists!", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(UserDTO.toDto(savedUser), HttpStatus.OK);
    }
}
