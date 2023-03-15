package com.example.familybudget.rest.v1;

import com.example.familybudget.dto.CredentialRequestDTO;
import com.example.familybudget.dto.JsonViews;
import com.example.familybudget.entity.UserEntity;
import com.example.familybudget.repository.UserRepository;
import com.example.familybudget.security.JwtTokenProvider;
import com.example.familybudget.service.UserService;
import com.example.familybudget.utils.Utils;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final Utils utils;

    @PostMapping("/registration")
    @JsonView(JsonViews.FullView.class)
    public ResponseEntity<?> registration(@RequestBody CredentialRequestDTO requestDTO) {
        return utils.saveNewUser(requestDTO);
    }

    @GetMapping("/activate/{code}")
    public ResponseEntity<?> activate(@PathVariable String code) {
        boolean isActivated = userService.activateUser(code);
        if (isActivated) {
            return new ResponseEntity<>("User activated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Activation code is not found", HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/login")
    @JsonView(JsonViews.ShortView.class)
    public ResponseEntity<?> authenticate(@RequestBody CredentialRequestDTO request){

        try {
            String email = request.getEmail();
            String password = request.getPassword();
            UserEntity userEntity = userRepository.getByEmail(email);

            if (userEntity == null || userEntity.getStatus().name().equals("DELETED")){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            String token = jwtTokenProvider.createToken(email, userEntity.getRole().name());

            Map<Object, Object> response = new HashMap<>();
            response.put("email", email);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.FORBIDDEN);
        }

    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }
}
