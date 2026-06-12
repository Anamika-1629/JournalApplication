package dev.anamika.journalApp.controllers;

import dev.anamika.journalApp.models.JournalEntry;
import dev.anamika.journalApp.repositories.JournalEntryRepository;
import dev.anamika.journalApp.services.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping("/journal-entries")
    public List<JournalEntry> getAll(){
        return journalEntryService.getAllEntries();
    }

    @PostMapping("/create-entries")
    public JournalEntry createEntry(@RequestBody JournalEntry myEntry){
        myEntry.setCreatedAt(LocalDateTime.now());
        journalEntryService.saveEntry(myEntry);
        return myEntry;
    }

    @GetMapping("/v1/find-entry/{id}")
    public Optional<JournalEntry> getEntryById(@PathVariable ObjectId id){
        return journalEntryService.getOneEntry(id);
    }

    @DeleteMapping("/v1/del-entry/{id}")
    public boolean delEntryById(@PathVariable ObjectId id){
        journalEntryService.delEntry(id);
        return true;
    }

    @PutMapping("/v1/update-entry/{id}")
    public JournalEntry updateEntryById(@PathVariable ObjectId id, @RequestBody JournalEntry newEntry){
        JournalEntry old = journalEntryService.getOneEntry(id).orElse(null);
        if (old != null) {
            old.setUpdatedAt(LocalDateTime.now());
            old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle());
            old.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : old.getContent());

            journalEntryService.saveEntry(old);
            return old;
        }
        return null;
    }
}