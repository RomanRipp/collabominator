package com.roman.ripp.collab;

import java.util.regex.Pattern;

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
        return Pattern.compile(Pattern.quote("(overdue)"), Pattern.CASE_INSENSITIVE)
                .matcher(displayText)
                .find();
    }
}
