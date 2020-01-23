package com.roman.ripp.collab;

import static com.roman.ripp.Utility.StringContainsIgnoreCase;

public class Participant {
    public String role;
    public String user;

    public boolean IsReviewer() {
        return StringContainsIgnoreCase(role, "REVIEWER");
    }
}
