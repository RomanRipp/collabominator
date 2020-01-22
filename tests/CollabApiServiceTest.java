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
        var credentialsFile = new File("testres/Credentials.json");
        var credentialsFilePath = credentialsFile.getAbsolutePath();
        try {
            var contensts = Files.readString(Paths.get(credentialsFilePath));
            var jsonObj = new JSONObject(contensts);
            var reviews = apiService.GetReviews(jsonObj.getString("userId"), jsonObj.getString("ticket"));
            assertTrue(reviews != null);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
}