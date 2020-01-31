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

    public ReviewObserver(String credentialsPath) {
        if (credentialsPath != null) {
            var credentials = ReadCredentials(credentialsPath);
            this.mCollabService = new CollabApiService(credentials);
        }
    }

    public ReviewObserver(CollabApiService apiService) {
        this.mCollabService = apiService;
    }

    public void HandleBadReviewers(
            HashMap<String, ArrayList<ActionItem>> badReviewersToActionItems,
            RandomizedPhraseGenerator phraseGenerator,
            TextToSpeechService ttsService) {
        if (!badReviewersToActionItems.isEmpty()) {
            ttsService.Say(phraseGenerator.GenerateGreeting());
            if (badReviewersToActionItems.size() == 1) {
                var entry = badReviewersToActionItems.entrySet().iterator().next();
                var userId = entry.getKey();
                var overdueItems = entry.getValue();
                var phrase = phraseGenerator.GenerateActionItemsOverduePhrase(userId, overdueItems);
                ttsService.Say(phrase);
            } else {
                var badReviewerIds = badReviewersToActionItems.keySet();
                var phrase = phraseGenerator.GenerateActionItemsOverduePhrase(badReviewerIds);
                ttsService.Say(phrase);
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
            var ttsService = new TextToSpeechService();
            var badReviewersToActionItems = GetBadReviewersAndOverdueActionItems();
            HandleBadReviewers(badReviewersToActionItems, phraseGenerator, ttsService);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
