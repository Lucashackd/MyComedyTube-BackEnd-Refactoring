package br.com.mycomedytube.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VideoCreateDTO(
        @NotBlank(message = "The title should not be empty")
        @Size(min = 3, max = 75, message = "The title must contain between 3 and 75 characters")
        String title,

        @Size(max = 400, message = "The description must have less than 400 characters")
        String description,

        @NotBlank
        String path,
        
        String thumbnail
) {
}
