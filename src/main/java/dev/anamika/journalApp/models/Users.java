package dev.anamika.journalApp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    private ObjectId id;

    private String firstName;

    private String lastName;

    @Indexed(unique = true)
    @NonNull
    private String userName;

    private String email;

    @NonNull
    private String password;

    @DocumentReference
    private List<JournalEntry> entryIDs = new ArrayList<>();

    private List<String> roles;

    private boolean emailVerified;

    private String provider;
}