package com.roman.ripp.collab;

import static com.roman.ripp.Utility.StringContainsIgnoreCase;

public class ActionItem {
    public String text;
    public String reviewText;
    public String nextActionText;
    public String roleText;
    public String relativeUrl;
    public int reviewId;
    public boolean reviewNeedsCommit;
    public boolean requiresUserAction;

    public boolean IsOverdue() {
        return StringContainsIgnoreCase(reviewText, "(overdue)");
    }

}
