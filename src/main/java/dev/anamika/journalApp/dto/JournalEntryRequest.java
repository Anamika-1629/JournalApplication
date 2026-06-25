package dev.anamika.journalApp.dto;

import lombok.Data;

@Data
public class JournalEntryRequest {
    private String title;
    private String content;
}
