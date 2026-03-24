package com.hzokbe.hayai.auth.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record SignUpRequestDTO(
        @NotBlank(message = "username cannot be blank")
        @Length(max = 16, message = "username must be at most 16 characters")
        String username,
        @NotBlank(message = "e-mail cannot be blank")
        @Email(message = "e-mail must be valid")
        String email,
        @NotBlank(message = "password cannot be blank")
        @Length(max = 64, message = "password must be at most 64 characters")
        String password
) {
    public SignUpRequestDTO {
        if (username != null) {
            username = username.trim();
        }

        if (email != null) {
            email = email.trim();
        }

        if (password != null) {
            password = password.trim();
        }
    }
}
