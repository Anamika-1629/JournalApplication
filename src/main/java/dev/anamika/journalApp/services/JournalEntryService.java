package dev.anamika.journalApp.services;

import dev.anamika.journalApp.dto.JournalEntryRequest;
import dev.anamika.journalApp.dto.JournalEntryUpdateRequest;
import dev.anamika.journalApp.dto.JournalEntryResponse;
import dev.anamika.journalApp.exception.MethodAccessDeniedException;
import dev.anamika.journalApp.exception.ResourceNotFoundException;
import dev.anamika.journalApp.models.JournalEntry;
import dev.anamika.journalApp.models.Users;
import dev.anamika.journalApp.repositories.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    //Helper method -> Validating the ownership of an entry
    private JournalEntry validateOwnership(String userName, ObjectId id) {
        JournalEntry entry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entry not found"));

        Users user = userService.findByUsername(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean owns = user.getEntryIDs().stream()
                .anyMatch(e -> e.getId().equals(id));

        if (!owns) {
            log.warn("User '{}' attempted to access entry '{}' they do not own", userName, id);
            throw new MethodAccessDeniedException("Access Denied");
        }

        return entry;
    }

    //Helper method -> To send the Journal Entry in response to an api
    private JournalEntryResponse mapToResponse(JournalEntry entry) {
        return new JournalEntryResponse(
                entry.getId().toHexString(),
                entry.getTitle(),
                entry.getContent(),
                entry.getCreatedAt()
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

        log.info("User '{}' created new journal entry", userName);
        return mapToResponse(savedEntry);
    }

    //Get all owned entries
    public List<JournalEntryResponse> getAllEntries(String userName){
        Users user = userService.findByUsername(userName).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        log.info("User '{}' requested for all owned journal entries", userName);
        return user.getEntryIDs().stream()
                .map(this::mapToResponse).toList();
    }

    //Get one owned entry
    public JournalEntryResponse getOneEntry(ObjectId id, String userName) {
        JournalEntry entry = validateOwnership(userName, id);

        log.info("User '{}' requested for Journal Entry '{}'", userName, id);
        return mapToResponse(entry);
    }

    //Delete one entry
    @Transactional
    public void deleteEntry(ObjectId id, String userName) {
        JournalEntry entry = validateOwnership(userName, id);

        Users user = userService.findByUsername(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.getEntryIDs().removeIf(e -> e.getId().equals(id));
        userService.saveExistingUser(user);

        journalEntryRepository.delete(entry);

        log.info("Journal entry '{}' deleted by user '{}'", id, userName);
    }

    //Update entry
    @Transactional
    public JournalEntryResponse updateEntry(ObjectId id, String userName, JournalEntryUpdateRequest entry) {
        JournalEntry existingEntry = validateOwnership(userName, id);

        boolean updated = false;

        if (entry.getTitle() != null && !entry.getTitle().isBlank()){
            existingEntry.setTitle(entry.getTitle());
            updated = true;}
        if (entry.getContent() != null && !entry.getContent().isBlank()){
            existingEntry.setContent(entry.getContent());
            updated = true;}
        if (updated){
            existingEntry.setUpdatedAt(LocalDateTime.now());
        }

        if (!updated) return mapToResponse(existingEntry);

        JournalEntry updatedEntry = journalEntryRepository.save(existingEntry);
        log.info("Journal entry '{}' updated by user '{}'", id, userName);
        return mapToResponse(updatedEntry);
    }
}
