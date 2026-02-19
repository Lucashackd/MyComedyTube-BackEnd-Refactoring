package br.com.mycomedytube.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateDTO(

        @NotBlank(message = "The name should not be empty")
        @Size(max = 75, message = "The name should have less than 75 characters")
        String name,

        @NotBlank(message = "The email should not be empty")
        @Email(message = "The email format is invalid")
        String email,

        @NotBlank(message = "The password should not be empty")
        @Size(min = 6, message = "The password must contain at least 6 characters")
        String password,

        @NotBlank(message = "The password confirmation should not be empty")
        @Size(min = 6, message = "The password must contain at least 6 characters")
        String confirmPassword
) {
}
