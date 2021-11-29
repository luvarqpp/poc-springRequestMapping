package sk.qpp.poc.security.controller.payload;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class SignUpRequest {
    @NotBlank
    @Size(min = 2)
    private final String name;

    @NotBlank
    @Email
    private final String email;

    @NotBlank
    private final String password;
}
