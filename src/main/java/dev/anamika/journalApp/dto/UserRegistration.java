package dev.anamika.journalApp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistration {

    @NotBlank
    private String userName;
    @Email @NotBlank
    private String email;
    @Size(min = 8)
    private String password;
    @NotBlank
    private String firstName;
    private String lastName;
}
