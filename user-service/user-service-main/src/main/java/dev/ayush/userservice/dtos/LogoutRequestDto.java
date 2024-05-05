package dev.ayush.userservice.dtos;

import lombok.Data;

@Data
public class LogoutRequestDto {
    private String token;
}
