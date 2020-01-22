package com.roman.ripp.collab;

import java.util.regex.Pattern;

import static com.roman.ripp.Utility.StringContainsIgnoreCase;

public class Review {

    class CustomField {
        String name;
        String[] value;
    }

    String creationDate;
    String lastActivity;
    String reviewPhase;
    String displayText;
    String title;
    String groupGuid;
    String templateName;
    String deadline;
    String accessPolicy;
    CustomField[] customFields;
    CustomField[] internalCustomFields;
    int reviewId;

    public boolean IsOverdue() {
        return StringContainsIgnoreCase(displayText, "(overdue)");
    }
}
