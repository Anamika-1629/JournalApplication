package dev.anamika.journalApp.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateUser {
    private String userName;

    @Email
    private String email;

    private String firstName;
    private String lastName;
}
