package dev.anamika.journalApp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    private ObjectId id;

    @Indexed(unique = true)
    @NonNull
    private String userName;
    @NonNull
    private String password;

    @DocumentReference
    private List<JournalEntry> entryIDs = new ArrayList<>();

    private List<String> roles;
}