package com.roman.ripp.collab;

public class Credentials {
    public String userId;
    public String ticket;

    public boolean IsValid() {
        return !userId.isEmpty() && !ticket.isEmpty();
    }
}
