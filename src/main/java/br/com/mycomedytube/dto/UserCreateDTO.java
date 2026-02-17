package br.com.mycomedytube.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserCreateDTO {

    @NotBlank(message = "The name should not be empty")
    @Size(max = 75, message = "The name should have less than 75 characters")
    private String name;

    @NotBlank(message = "The email should not be empty")
    @Email(message = "The email format is invalid")
    private String email;

    @NotBlank(message = "The password should not be empty")
    @Size(min = 6, message = "The password must contain at least 6 characters")
    private String password;

    @NotBlank(message = "The password confirmation should not be empty")
    @Size(min = 6, message = "The password must contain at least 6 characters")
    private String confirmPassword;

    public UserCreateDTO() {
    }

    public UserCreateDTO(String name, String email, String password, String confirmPassword) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
