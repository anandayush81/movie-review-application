package dev.ayush.userservice.controllers;

import dev.ayush.userservice.dtos.LoginRequestDto;
import dev.ayush.userservice.dtos.LogoutRequestDto;
import dev.ayush.userservice.dtos.SignUpRequestDto;
import dev.ayush.userservice.dtos.UserDto;
import dev.ayush.userservice.dtos.UserToken;
import dev.ayush.userservice.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Login
    @PostMapping("/login")
    public UserToken login(@RequestBody LoginRequestDto loginRequestDto) {
        String token = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        return new UserToken(token);
    }

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        return UserDto.fromUser(authService.signUp(signUpRequestDto.getName(), signUpRequestDto.getEmail(), signUpRequestDto.getPassword()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto logoutRequestDto) {
        authService.logout(logoutRequestDto.getToken());
        return ResponseEntity.noContent().build();
    }

    // validate token
    @GetMapping("/validate-token")
    public ResponseEntity<UserDto> validateToken(@RequestParam("token") String token) {
        return ResponseEntity.ok(UserDto.fromUser(authService.validateToken(token)));
    }
}
