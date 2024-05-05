package dev.ayush.userservice.services;

import dev.ayush.userservice.dtos.SendEmailEventDto;
import dev.ayush.userservice.exceptions.InvalidTokenException;
import dev.ayush.userservice.exceptions.LoginFailedException;
import dev.ayush.userservice.exceptions.UserNotFoundException;
import dev.ayush.userservice.models.Token;
import dev.ayush.userservice.models.User;
import dev.ayush.userservice.repositories.TokenRepository;
import dev.ayush.userservice.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final TokenRepository tokenRepository;
    private final ObjectMapper objectMapper;

    public AuthService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       KafkaTemplate<String, String> kafkaTemplate,
                       TokenRepository tokenRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.kafkaTemplate = kafkaTemplate;
        this.tokenRepository = tokenRepository;
        this.objectMapper = objectMapper;
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        if (!bCryptPasswordEncoder.matches(password, user.getHashedPassword())) {
            throw new LoginFailedException("Invalid password");
        }
        Token token = new Token();
        token.setUser(user);
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(30);
        token.setExpiry(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        return tokenRepository.save(token).getValue();
    }

    public User signUp(String name, String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));
        user.setName(name);
        User savedUser = userRepository.save(user);
        SendEmailEventDto sendEmailEventDto = SendEmailEventDto.builder()
                .to(savedUser.getEmail())
                .from("info@yash.com")
                .subject("Welcome to our platform")
                .body("Thanks for signing up. We hope you enjoy our platform.")
                .build();
        try {
            kafkaTemplate.send("sendEmail", objectMapper.writeValueAsString(sendEmailEventDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return savedUser;
    }

    public void logout(String tokenValue) {
        Token token = tokenRepository.findByValue(tokenValue)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));
        tokenRepository.delete(token);
    }

    public User validateToken(String token) {
        Token token1 = tokenRepository.findByValue(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));
        if (token1.getExpiry().before(new Date())) {
            tokenRepository.delete(token1);
            throw new InvalidTokenException("Token expired");
        }
        return token1.getUser();
    }
}
