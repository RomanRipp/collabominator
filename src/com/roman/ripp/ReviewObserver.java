package com.roman.ripp;

import com.roman.ripp.collab.CollabApiService;
import com.roman.ripp.collab.Credentials;

import java.util.ArrayList;
import java.util.HashMap;

import static com.roman.ripp.Utility.ReadCredentials;
import static java.lang.Thread.sleep;

public class ReviewObserver {

    Credentials mCredentials;

    public ReviewObserver(String credentialsPath) {
        if (credentialsPath != null) {
            this.mCredentials = ReadCredentials(credentialsPath);
        }
    }

    public HashMap<String, ArrayList<Integer>> GetBadReviewersAndOverdueReviews(CollabApiService collabService) {
        var actionItems = collabService.GetActionItems();
        var badReviewerIdToReviewIds = new HashMap<String, ArrayList<Integer>>();
        for (var actionItem : actionItems) {
            if (actionItem.IsOverdue()) {
                var badReviewers = collabService.GetReviewParticipants(actionItem.reviewId);
                badReviewers.removeIf(r -> (!r.IsReviewer()));
                for (var badReviewer : badReviewers) {
                    var badReviewerId = badReviewer.user;
                    var reviewId = actionItem.reviewId;
                    badReviewerIdToReviewIds.putIfAbsent(badReviewerId, new ArrayList<Integer>());
                    badReviewerIdToReviewIds.get(badReviewerId).add(reviewId);
                }
            }
        }
        return badReviewerIdToReviewIds;
    }

    public void RunOnce()
    {
        var collabService = new CollabApiService(mCredentials);
        var actionItems = collabService.GetActionItems();
        var badReviewerIdToReviewIds = new HashMap<String, ArrayList<Integer>>();
        for (var actionItem : actionItems) {
            if (actionItem.IsOverdue()) {
                var badReviewers = collabService.GetReviewParticipants(actionItem.reviewId);
                badReviewers.removeIf(r -> (!r.IsReviewer()));
                for (var badReviewer : badReviewers) {
                    var badReviewerId = badReviewer.user;
                    var reviewId = actionItem.reviewId;
                    badReviewerIdToReviewIds.putIfAbsent(badReviewerId, new ArrayList<Integer>());
                    badReviewerIdToReviewIds.get(badReviewerId).add(reviewId);
                }
            }
        }
    }

    public void Run() {
        while (true) {
            try {
                RunOnce();
                sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
