package dev.anamika.journalApp.services;

import dev.anamika.journalApp.dto.JournalEntryRequest;
import dev.anamika.journalApp.models.JournalEntry;
import dev.anamika.journalApp.models.Users;
import dev.anamika.journalApp.repositories.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    //Create new entry
    @Transactional
    public JournalEntry saveEntry(JournalEntry journalEntry, String userName) {
        Users user = userService.findByUsername(userName).
                orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));

        journalEntry.setUserId(user.getId());
        journalEntry.setCreatedAt(LocalDateTime.now());
        JournalEntry entry = journalEntryRepository.save(journalEntry);

        user.getEntryIDs().add(entry);
        userService.saveUser(user);

        return entry;
    }

    //Get all owned entries
    public List<JournalEntry> getAllEntries(String userName){
        Users user = userService.findByUsername(userName).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return user.getEntryIDs();
    }

    //Get one owned entry
    public Optional<JournalEntry> getOneEntry(ObjectId id, String userName){
        validateOwnership(userName, id);

        return journalEntryRepository.findById(id);
    }

    //Delete one entry
    public void deleteEntry(ObjectId id, String userName){
        Users user = validateOwnership(userName, id);

        user.getEntryIDs().removeIf(e -> e.getId().equals(id));
        userService.saveUser(user);
        journalEntryRepository.deleteById(id);
    }

    //Update entry
    public JournalEntry updateEntry(ObjectId id, String userName, JournalEntryRequest entry) {
        validateOwnership(userName,id);
        JournalEntry old = journalEntryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry Not Found"));

        old.setUpdatedAt(LocalDateTime.now());

        if (entry.getTitle() != null && !entry.getTitle().isBlank())
            old.setTitle(entry.getTitle());
        if (entry.getContent() != null && !entry.getContent().isBlank())
            old.setContent(entry.getContent());

        journalEntryRepository.save(old);
        return old;
    }

    private Users validateOwnership(String userName, ObjectId id){
        Users u = userService.findByUsername(userName).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        boolean owns = u.getEntryIDs().stream().anyMatch(e -> e.getId().equals(id));
        if (!owns) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");

        return u;
    }
}
