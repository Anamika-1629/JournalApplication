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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("/v1/journal-entries/{userName}")
    public ResponseEntity<List<JournalEntry>> getAll(@PathVariable String userName){
        Users user = userService.findByUsername(userName).orElse(null);
        if (user != null) return new ResponseEntity<>(user.getEntryIDs(), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/v1/create-entries/{userName}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String userName){
        myEntry.setCreatedAt(LocalDateTime.now());
        try {
            JournalEntry saved = journalEntryService.saveEntry(myEntry, userName);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        }
        catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/v1/find-entry/{userName}/{id}")
    public ResponseEntity<JournalEntry> getEntryById(@PathVariable ObjectId id, @PathVariable String userName){
        Users user = userService.findByUsername(userName).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        boolean owns = user.getEntryIDs().stream().anyMatch(e -> e.getId().equals(id));

        if (!owns) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return journalEntryService.getOneEntry(id)
                .map(entry -> new ResponseEntity<>(entry, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/v1/del-entry/{userName}/{id}")
    public ResponseEntity<?> delEntryById(@PathVariable ObjectId id, @PathVariable String userName){
        boolean deleted = journalEntryService.delEntry(id, userName);
        if (deleted) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>("Entry not found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/v1/update-entry/{userName}/{id}")
    public ResponseEntity<JournalEntry> updateEntryById(@PathVariable ObjectId id, @RequestBody JournalEntry newEntry, @PathVariable String userName){

        Users user = userService.findByUsername(userName).orElse(null);
        if (user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        boolean owns = user.getEntryIDs().stream().anyMatch(e -> e.getId().equals(id));
        if (!owns) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        JournalEntry old = journalEntryService.getOneEntry(id).orElse(null);
        if (old == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (newEntry.getTitle() != null && !newEntry.getTitle().isEmpty()) old.setTitle(newEntry.getTitle());
        if (newEntry.getContent() != null && !newEntry.getContent().isEmpty()) old.setContent(newEntry.getContent());
        old.setUpdatedAt(LocalDateTime.now());

        journalEntryService.saveEntry(old, userName);
        return new ResponseEntity<>(old, HttpStatus.OK);
    }
}
