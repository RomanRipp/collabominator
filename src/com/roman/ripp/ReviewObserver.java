package com.roman.ripp;

import com.roman.ripp.collab.ActionItem;
import com.roman.ripp.collab.CollabApiService;
import com.roman.ripp.collab.Credentials;
import com.roman.ripp.collab.Review;

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

    public HashMap<String, ArrayList<ActionItem>> GetBadReviewersAndOverdueActionItems(CollabApiService collabService) {
        var actionItems = collabService.GetActionItems();
        var badReviewerIdToReviews = new HashMap<String, ArrayList<ActionItem>>();
        for (var actionItem : actionItems) {
            if (actionItem.IsOverdue()) {
                var badReviewers = collabService.GetReviewParticipants(actionItem.reviewId);
                badReviewers.removeIf(r -> (!r.IsReviewer()));
                for (var badReviewer : badReviewers) {
                    var badReviewerId = badReviewer.user;
                    badReviewerIdToReviews.putIfAbsent(badReviewerId, new ArrayList<ActionItem>());
                    badReviewerIdToReviews.get(badReviewerId).add(actionItem);
                }
            }
        }
        return badReviewerIdToReviews;
    }

    public void RunOnce()
    {
        var collabService = new CollabApiService(mCredentials);
        var badReviewersToActionItems = GetBadReviewersAndOverdueActionItems(collabService);

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
