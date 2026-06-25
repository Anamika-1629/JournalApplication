package dev.anamika.journalApp.dto;

import lombok.Data;

@Data
public class JournalEntryUpdateRequest {
    private String title;
    private String content;
}
