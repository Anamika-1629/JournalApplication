package dev.anamika.journalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> roles;
}
