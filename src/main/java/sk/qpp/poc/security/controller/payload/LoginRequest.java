package sk.qpp.poc.security.controller.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank
    @Email
    private final String email;

    @NotBlank
    private final String password;
}
