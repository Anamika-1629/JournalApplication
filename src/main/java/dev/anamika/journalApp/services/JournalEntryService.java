package dev.anamika.journalApp.services;

import dev.anamika.journalApp.models.JournalEntry;
import dev.anamika.journalApp.repositories.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    //get all entries
    public List<JournalEntry> getAllEntries(){
        return journalEntryRepository.findAll();
    }

    //get one entry, find by its object id, also takes title --> in url, it will show title
    public Optional<JournalEntry> getOneEntry(ObjectId id){
        return journalEntryRepository.findById(id);
    }

    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    public void delEntry(ObjectId id){
        journalEntryRepository.deleteById(id);
    }
}