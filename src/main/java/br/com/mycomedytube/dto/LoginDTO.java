package br.com.mycomedytube.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class LoginDTO implements Serializable {

    @NotBlank(message = "The email should not be empty")
    @Email(message = "The email format is invalid")
    private String email;

    @NotBlank(message = "The password should not be empty")
    @Size(min = 6, message = "The password must contain at least 6 characters")
    private String password;

    public LoginDTO() {
    }

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
