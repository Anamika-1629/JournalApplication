package dev.anamika.journalApp.services;

import dev.anamika.journalApp.models.JournalEntry;
import dev.anamika.journalApp.models.Users;
import dev.anamika.journalApp.repositories.JournalEntryRepository;
import org.apache.catalina.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    public List<JournalEntry> getAllEntries(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> getOneEntry(ObjectId id){
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public JournalEntry saveEntry(JournalEntry journalEntry, String userName) {
        Users user = userService.findByUsername(userName).
                orElseThrow(()-> new RuntimeException("User Not Found: "+userName));

        //set user's id ref in journal entry model
        journalEntry.setUserId(user.getId());
        JournalEntry entry = journalEntryRepository.save(journalEntry);

        user.getEntryIDs().add(entry);
        userService.saveUser(user);

        return entry;
    }

    public boolean delEntry(ObjectId id, String userName){
        Users user = userService.findByUsername(userName).orElse(null);
        if (user == null) return false;

        boolean removed = user.getEntryIDs().removeIf(e -> e.getId().equals(id));
        if (!removed) return false;

        userService.saveUser(user);
        journalEntryRepository.deleteById(id);
        return true;
    }
}
