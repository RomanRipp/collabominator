import com.roman.ripp.collab.CollabApiService;
import com.roman.ripp.collab.Credentials;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        var credentials = ReadCredentials();
        if (credentials != null) {
            var apiService = new CollabApiService(credentials);
            var review = apiService.FindReview(14489);
            assertNotNull(review);
        }
    }

    @Test
    void GetReviewParticipants() {
        var credentials = ReadCredentials();
        if (credentials != null) {
            var apiService = new CollabApiService(credentials);
            var participants = apiService.GetReviewParticipants(14489);
            assertNotNull(participants);
            assertTrue(participants.size() > 0);
        }
    }

    @Test
    void GetReviews() {
        var credentials = ReadCredentials();
        if (credentials != null) {
            var apiService = new CollabApiService(credentials);
            var reviews = apiService.GetReviews();
            assertNotNull(reviews);
        }
    }

    private Credentials ReadCredentials() {
        var credentialsFile = new File("testres/com.roman.ripp.collab.Credentials.json");
        if (credentialsFile.exists()) {
            var credentialsFilePath = credentialsFile.getAbsolutePath();
            try {
                var contents = Files.readString(Paths.get(credentialsFilePath));
                return new Gson().fromJson(contents, Credentials.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}