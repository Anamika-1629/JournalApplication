package dev.anamika.journalApp.controllers;

import dev.anamika.journalApp.models.JournalEntry;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    private final Map<Long, JournalEntry> journalEntries = new HashMap<>();

    @GetMapping("/journal-entries")
    public List<JournalEntry> getAll(){
        return new ArrayList<>(journalEntries.values());
    }

    @PostMapping("/create-entries")
    public boolean createEntry(@RequestBody JournalEntry myEntry){
        journalEntries.put(myEntry.getId(), myEntry);
        return true;
    }

    @GetMapping("/id/{id}")
    public JournalEntry getEntryById(@PathVariable long id){
        return journalEntries.get(id);
    }

    @DeleteMapping("/id/{id}")
    public boolean delEntryById(@PathVariable long id){
        return true;
    }

    @PutMapping("/id/{id}")
    public JournalEntry updateEntryById(@PathVariable long id, @RequestBody JournalEntry myEntry){
        return journalEntries.put(id, myEntry);
    }
}