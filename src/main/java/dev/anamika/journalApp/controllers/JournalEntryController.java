package dev.anamika.journalApp.controllers;

import dev.anamika.journalApp.models.JournalEntry;
import dev.anamika.journalApp.models.Users;
import dev.anamika.journalApp.repositories.JournalEntryRepository;
import dev.anamika.journalApp.services.JournalEntryService;
import dev.anamika.journalApp.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("/v1/journal-entries")
    public ResponseEntity<List<JournalEntry>> getAll(Authentication authentication){
        Users user = userService.findByUsername(authentication.getName()).orElse(null);
        if (user != null) return new ResponseEntity<>(user.getEntryIDs(), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/v1/create-entries")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry, Authentication authentication){
        JournalEntry saved = journalEntryService.saveEntry(myEntry, authentication.getName());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/v1/find-entry/{id}")
    public ResponseEntity<JournalEntry> getEntryById(@PathVariable ObjectId id, Authentication authentication){
        JournalEntry entry = journalEntryService.getOneEntry(id, authentication.getName()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry Not Found"));
        return new ResponseEntity<>(entry, HttpStatus.OK);
    }

    @DeleteMapping("/v1/delete-entry/{id}")
    public ResponseEntity<?> delEntryById(@PathVariable ObjectId id, Authentication authentication){
        journalEntryService.deleteEntry(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/v1/update-entry/{id}")
    public ResponseEntity<JournalEntry> updateEntryById(@PathVariable ObjectId id, @RequestBody JournalEntry newEntry, Authentication authentication){
        JournalEntry entry = journalEntryService.updateEntry(id, authentication.getName(), newEntry);
        return new ResponseEntity<>(entry, HttpStatus.OK);
    }
}
