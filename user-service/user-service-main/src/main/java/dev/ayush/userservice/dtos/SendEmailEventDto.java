package dev.ayush.userservice.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SendEmailEventDto {
    private String to;
    private String from;
    private String subject;
    private String body;
}
