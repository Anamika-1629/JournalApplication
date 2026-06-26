package dev.anamika.journalApp.services;

import dev.anamika.journalApp.dto.JournalEntryRequest;
import dev.anamika.journalApp.dto.JournalEntryUpdateRequest;
import dev.anamika.journalApp.dto.JournalEntryResponse;
import dev.anamika.journalApp.exception.MethodAccessDeniedException;
import dev.anamika.journalApp.exception.ResourceNotFoundException;
import dev.anamika.journalApp.models.JournalEntry;
import dev.anamika.journalApp.models.Users;
import dev.anamika.journalApp.repositories.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    //Helper method -> Validating the ownership of an entry
    private Users validateOwnership(String userName, ObjectId id){
        Users u = userService.findByUsername(userName).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean owns = u.getEntryIDs().stream().anyMatch(e -> e.getId().equals(id));
        if (!owns) throw new MethodAccessDeniedException("Access Denied");

        return u;
    }

    //Helper method -> To send the Journal Entry in response to an api
    private JournalEntryResponse mapToResponse(JournalEntry entry) {
        return new JournalEntryResponse(
                entry.getId().toHexString(),
                entry.getTitle(),
                entry.getContent(),
                entry.getCreatedAt(),
                entry.getUpdatedAt()
        );
    }

    //Create new entry
    @Transactional
    public JournalEntryResponse saveEntry(JournalEntryRequest dto, String userName) {
        Users user = userService.findByUsername(userName).
                orElseThrow(()-> new ResourceNotFoundException("User Not Found"));

        JournalEntry journalEntry = new JournalEntry();

        journalEntry.setUserId(user.getId());
        journalEntry.setCreatedAt(LocalDateTime.now());
        journalEntry.setTitle(dto.getTitle());
        journalEntry.setContent(dto.getContent());
        JournalEntry savedEntry = journalEntryRepository.save(journalEntry);

        user.getEntryIDs().add(savedEntry);
        userService.saveExistingUser(user);

        return mapToResponse(savedEntry);
    }

    //Get all owned entries
    public List<JournalEntryResponse> getAllEntries(String userName){
        Users user = userService.findByUsername(userName).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        return user.getEntryIDs().stream()
                .map(this::mapToResponse).toList();
    }

    //Get one owned entry
    public JournalEntryResponse getOneEntry(ObjectId id, String userName) {
        validateOwnership(userName, id);

        JournalEntry entry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entry Not Found"));

        return mapToResponse(entry);
    }

    //Delete one entry
    public void deleteEntry(ObjectId id, String userName){
        Users user = validateOwnership(userName, id);

        user.getEntryIDs().removeIf(e -> e.getId().equals(id));
        userService.saveExistingUser(user);
        journalEntryRepository.deleteById(id);
    }

    //Update entry
    public JournalEntryResponse updateEntry(ObjectId id, String userName, JournalEntryUpdateRequest entry) {
        validateOwnership(userName,id);
        JournalEntry old = journalEntryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entry Not Found"));

        boolean updated = false;

        if (entry.getTitle() != null && !entry.getTitle().isBlank()){
            old.setTitle(entry.getTitle());
            updated = true;}
        if (entry.getContent() != null && !entry.getContent().isBlank()){
            old.setContent(entry.getContent());
            updated = true;}
        if (updated){
            old.setUpdatedAt(LocalDateTime.now());
        }

        JournalEntry updatedEntry = journalEntryRepository.save(old);
        return mapToResponse(updatedEntry);
    }
}
