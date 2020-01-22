package com.roman.ripp.collab;

import org.junit.jupiter.api.Test;
import static com.roman.ripp.Utility.ReadCredentials;
import static org.junit.jupiter.api.Assertions.*;

class CollabApiServiceTest {

    @Test
    void ServerVersion() {
        var apiService = new CollabApiService(null);
        var version = apiService.GetVersion();
        assertEquals(version, "12.3.12302");
    }

    @Test
    void GetReview() {
        var credentials = ReadCredentials("testres/Credentials.json");
        if (credentials != null) {
            var apiService = new CollabApiService(credentials);
            var review = apiService.FindReview(14489);
            assertNotNull(review);
        }
    }

    @Test
    void GetReviewParticipants() {
        var credentials = ReadCredentials("testres/Credentials.json");
        if (credentials != null) {
            var apiService = new CollabApiService(credentials);
            var participants = apiService.GetReviewParticipants(14489);
            assertNotNull(participants);
            assertTrue(participants.size() > 0);
        }
    }

    @Test
    void GetReviews() {
        var credentials = ReadCredentials("testres/Credentials.json");
        if (credentials != null) {
            var apiService = new CollabApiService(credentials);
            var reviews = apiService.GetReviews();
            assertNotNull(reviews);
        }
    }
}