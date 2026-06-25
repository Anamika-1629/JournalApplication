package dev.anamika.journalApp.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePassword {

    @Size(min = 8)
    private String oldPassword;
    @Size(min = 8)
    private String newPassword;
}
