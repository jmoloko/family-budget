package com.example.familybudget.rest.v1;

import com.example.familybudget.dto.JsonViews;
import com.example.familybudget.dto.UserDTO;
import com.example.familybudget.entity.UserEntity;
import com.example.familybudget.exception.UserNotFoundException;
import com.example.familybudget.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/user")
public class UserRestController {

    private final UserService userService;

    @GetMapping
    @JsonView(JsonViews.ShortView.class)
    public ResponseEntity<?> getUserById(Authentication authentication) {
        String name = authentication.getName();

        UserEntity user;
        try {
            user = userService.getByEmail(name);
            UserDTO result = UserDTO.toDto(user);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



}
