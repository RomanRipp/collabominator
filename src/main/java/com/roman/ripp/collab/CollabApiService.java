package com.roman.ripp.collab;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

public class CollabApiService {

    String mServerUrl = "https://codecollaborator.dptechnology.com/services/json/v1";
    Credentials mCredentials;

    public CollabApiService(Credentials mCredentials) {
        this.mCredentials = mCredentials;
    }

    private JSONObject GetAuth() {
        var args = new JSONObject()
                .put("login", mCredentials.userId)
                .put("ticket", mCredentials.ticket);
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
        var getVersionObj = new JSONObject().put("command", "ServerInfoService.getVersion");
        var request = new JSONArray().put(getVersionObj);
        var response = Post(request);
        var responseObj = (JSONObject) response.get(0);
        return responseObj.getJSONObject("result").get("version").toString();
    }

    void CheckCredentials() {
        if (mCredentials == null || !mCredentials.IsValid()) {
            throw new RuntimeException("Valid credentials not provided.");
        }

        var request = new JSONArray().put(GetAuth());
        var response = Post(request).getJSONObject(0);
        if (response.has("errors")) {
            throw new RuntimeException("Valid credentials not provided.");
        }
    }

    public Review FindReview(int reviewId) {
        CheckCredentials();
        var reviewIdObj = new JSONObject().put("reviewId", reviewId);
        var findReviewByIdObj = new JSONObject()
                .put("command", "ReviewService.findReviewById")
                .put("args", reviewIdObj);
        var request = new JSONArray()
                .put(GetAuth())
                .put(findReviewByIdObj);
        var response = Post(request).getJSONObject(1);
        if (response.has("result")) {
            var jsonReviewObj = response.getJSONObject("result");
            var review = new Gson().fromJson(jsonReviewObj.toString(), Review.class);
            return review;
        }
        return null;
    }

    public ArrayList<ActionItem> GetActionItems() {
        CheckCredentials();
        var getActionItems = new JSONObject()
                .put("command", "UserService.getActionItems");
        var request = new JSONArray()
                .put(GetAuth())
                .put(getActionItems);
        var response = Post(request);

        var gson = new Gson();
        var result = new ArrayList<ActionItem>();

        var actionItemsArray = response.getJSONObject(1)
                .getJSONObject("result")
                .getJSONArray("actionItems");

        for (Object obj : actionItemsArray) {
            var jsonObj = (JSONObject) obj;
            var actionItem = gson.fromJson(jsonObj.toString(), ActionItem.class);
            if (actionItem != null) {
                result.add(actionItem);
            }
        }
        return result;
    }

    public ArrayList<Review> GetReviews() {
        var result = new ArrayList<Review>();
        var actionItems = GetActionItems();
        for (var actionItem : actionItems) {
            var review = FindReview(actionItem.reviewId);
            if (review != null) {
                result.add(review);
            }
        }
        return result;
    }

    public ArrayList<Participant> GetReviewParticipants(int reviewId) {
        var reviewIdObj = new JSONObject().put("reviewId", reviewId);
        var getParticipantsObj = new JSONObject()
                .put("command", "ReviewService.getParticipants")
                .put("args", reviewIdObj);
        var request = new JSONArray()
                .put(GetAuth())
                .put(getParticipantsObj);
        var response = Post(request).getJSONObject(1);
        var result = new ArrayList<Participant>();
        if (response.has("result")) {
            var gson = new Gson();
            var participantJsonObjs = response.getJSONObject("result").getJSONArray("assignments");
            for (var participantJsonObj : participantJsonObjs) {
                var participant = gson.fromJson(participantJsonObj.toString(), Participant.class);
                result.add(participant);
            }
        }
        return result;
    }
}
