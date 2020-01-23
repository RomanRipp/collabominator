package com.roman.ripp.speech;

import com.roman.ripp.collab.ActionItem;

import java.util.ArrayList;
import java.util.Random;

public class RandomizedPhraseGenerator {

    private String GenerateRandomPrefixPhrase() {
        var prefixOptions = new ArrayList<String>() {{
            add("Roses are red violets are blue ");
            add("This happened ");
            add("A bat flew in my window and told me ");
        }};
        var selector = new Random().nextInt(prefixOptions.size() - 1);
        return prefixOptions.get(selector);
    }

    private String GenerateLateReviewsPhrase() {
        return GenerateRandomPrefixPhrase() + "%s is late on his reviews!";
    }

    private String GenerateLateReviewPhrase() {
        return GenerateRandomPrefixPhrase() + "%s is late on his review!";
    }

    public String GenerateActionItemsOverduePhrase(String userId, ArrayList<ActionItem> overdueActionItems) {
        if (overdueActionItems.isEmpty()) {
            throw new RuntimeException("Overdue items are empty!");
        }
        var phrase = (overdueActionItems.size() > 1) ?
                GenerateLateReviewsPhrase() : GenerateLateReviewPhrase();
        var userName = ExtractUserName(userId);
        return String.format(phrase, userName);
    }

    public static String ExtractUserName(String userId) {
        var arr = userId.trim().split("[\\s@&.?$+-]+");
        if (arr.length >= 2) {
            return (arr[0] + " " + arr[1]).trim();
        } else {
            return userId.trim();
        }
    }

}
