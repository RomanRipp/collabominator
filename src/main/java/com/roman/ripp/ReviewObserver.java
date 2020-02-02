package com.roman.ripp;

import com.roman.ripp.collab.ActionItem;
import com.roman.ripp.collab.CollabApiService;
import com.roman.ripp.speech.RandomizedPhraseGenerator;
import com.roman.ripp.speech.TextToSpeechService;

import java.util.ArrayList;
import java.util.HashMap;

import static com.roman.ripp.Utility.ReadCredentials;

public class ReviewObserver {

    CollabApiService mCollabService;
    TextToSpeechService mTtsService;

    public ReviewObserver(String credentialsPath) {
        if (credentialsPath != null) {
            var credentials = ReadCredentials(credentialsPath);
            this.mCollabService = new CollabApiService(credentials);
            this.mTtsService = new TextToSpeechService();
        }
    }

    public ReviewObserver(CollabApiService apiService, TextToSpeechService ttsService) {
        this.mCollabService = apiService;
        this.mTtsService = ttsService;
    }

    public void HandleBadReviewers(
            HashMap<String, ArrayList<ActionItem>> badReviewersToActionItems,
            RandomizedPhraseGenerator phraseGenerator) {
        if (!badReviewersToActionItems.isEmpty()) {
            mTtsService.Say(phraseGenerator.GenerateGreeting());
            if (badReviewersToActionItems.size() == 1) {
                var entry = badReviewersToActionItems.entrySet().iterator().next();
                var userId = entry.getKey();
                var overdueItems = entry.getValue();
                var phrase = phraseGenerator.GenerateActionItemsOverduePhrase(userId, overdueItems);
                mTtsService.Say(phrase);
            } else {
                var badReviewerIds = badReviewersToActionItems.keySet();
                var phrase = phraseGenerator.GenerateActionItemsOverduePhrase(badReviewerIds);
                mTtsService.Say(phrase);
            }
        }
    }

    public HashMap<String, ArrayList<ActionItem>> GetBadReviewersAndOverdueActionItems() {
        var actionItems = mCollabService.GetActionItems();
        var badReviewerIdToReviews = new HashMap<String, ArrayList<ActionItem>>();
        for (var actionItem : actionItems) {
            if (actionItem.IsOverdue()) {
                var badReviewers = mCollabService.GetReviewParticipants(actionItem.reviewId);
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
        try {
            var phraseGenerator = new RandomizedPhraseGenerator();
            var badReviewersToActionItems = GetBadReviewersAndOverdueActionItems();
            HandleBadReviewers(badReviewersToActionItems, phraseGenerator);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
