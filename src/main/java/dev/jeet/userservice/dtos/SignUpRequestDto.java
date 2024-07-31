package dev.jeet.userservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {
    String name;
    String email;
    String password;
}
