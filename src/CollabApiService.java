import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

public class CollabApiService {

    String mServerUrl = "https://codecollaborator.dptechnology.com/services/json/v1";

    private static JSONObject GetAuth(String userId, String ticket) {
        var args = new JSONObject()
                .put("login", userId)
                .put("ticket", ticket);
        return new JSONObject()
                .put("command", "SessionService.authenticate")
                .put("args", args);
    }

    private JSONArray Post(JSONArray request) {
        String response = null;
        HttpURLConnection connection = null;

        try {
            var url = new URL(mServerUrl);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            byte[] input = request.toString().getBytes("utf-8");
            os.write(input, 0, input.length);

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuilder buffer = new StringBuilder();

            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                buffer.append(responseLine.trim());
            }

            response = buffer.toString();

        } catch (Exception e) {
            Logger.getGlobal().severe(e.getMessage());
        }
        finally {
            connection.disconnect();
        }

        if (response == null)
        {
            throw new RuntimeException("Post failed");
        }

        return new JSONArray(response);
    }

    public String GetVersion() {
        String version = null;

        var request = new JSONArray().put(new JSONObject().put("command", "ServerInfoService.getVersion"));

        var response = Post(request);

        var responseObj = (JSONObject) response.get(0);
        version = ((JSONObject) responseObj.get("result")).get("version").toString();

        return version;
    }

    public ArrayList<Review> GetReviews(String userId, String ticket) {
        var getActionItems = new JSONObject().put("command", "UserService.getActionItems");
        var request = new JSONArray()
                .put(GetAuth(userId, ticket))
                .put(getActionItems);

        var response = Post(request);

        var result = new ArrayList<Review>();
        var gson = new Gson();
        var actionItemsArray = response.getJSONObject(1).getJSONObject("result").getJSONArray("actionItems");
        for (Object obj : actionItemsArray) {
            var jsonObj = (JSONObject) obj;
            var review = gson.fromJson(jsonObj.toString(), Review.class);
            result.add(review);
        }

        return result;
    }
}
