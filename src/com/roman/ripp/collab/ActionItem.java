package com.roman.ripp.collab;

import static com.roman.ripp.Utility.StringContainsIgnoreCase;

public class ActionItem {
    String text;
    String reviewText;
    String nextActionText;
    String roleText;
    String relativeUrl;
    public int reviewId;
    boolean reviewNeedsCommit;
    boolean requiresUserAction;

    public boolean IsOverdue() {
        return StringContainsIgnoreCase(reviewText, "(overdue)");
    }

}
