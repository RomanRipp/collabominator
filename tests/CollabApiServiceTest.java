import com.google.gson.Gson;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class CollabApiServiceTest {

    @Test
    void ServerVersion() {
        var apiService = new CollabApiService();
        var version = apiService.GetVersion();
        assertEquals(version, "12.3.12302");
    }

    @Test
    void GetReviews() {
        var apiService = new CollabApiService();
        var credentials = ReadCredentials();
        if (credentials != null) {
            var reviews = apiService.GetReviews(credentials.userId, credentials.ticket);
            assertTrue(reviews != null);
        }
    }

    private class Credentials {
        public String userId;
        public String ticket;
    }

    private Credentials ReadCredentials() {
        var credentialsFile = new File("testres/Credentials.json");
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