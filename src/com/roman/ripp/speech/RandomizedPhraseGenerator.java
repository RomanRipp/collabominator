package com.roman.ripp.speech;

import com.roman.ripp.collab.ActionItem;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class RandomizedPhraseGenerator {

    private String GetRandomSelection(ArrayList<String> options) {
        var selector = new Random().nextInt(options.size());
        return options.get(selector);
    }

    private String GenerateRandomPrefixPhrase() {
        var prefixOptions = new ArrayList<String>() {{
            add("This happened, ");
            add("Roses are red, violets are blue, ");
            add("This happened again, ");
        }};
        return GetRandomSelection(prefixOptions);
    }

    public String GenerateGreeting() {
        var greetingsOptions = new ArrayList<String>() {{
                add("Top of the morning to you Humans!");
                add("What is cracking Humans?");
                add("GOOOOOD MORNING, HUMANS!");
                add("This call may be recorded for training purposes.");
                add("Howdy, howdy ,howdy!");
                add("I'm Batman");
                add("At least, we meet for the first time for the last time!");
                add("Hello, who's there, I'm talking.");
                add("You know who this is.");
                add("Yo, yo, yo Humans");
                add("What is up, Humans");
                add("Greetings and salutations, Humans!");
            }};
        return GetRandomSelection(greetingsOptions);    }

    private String GenerateLateReviewsPhrase() {
        return GenerateRandomPrefixPhrase() + "%s is late on his code reviews";
    }

    private String GenerateLateReviewPhrase() {
        return GenerateRandomPrefixPhrase() + "%s is late on his code review";
    }

    public String GenerateActionItemsOverduePhrase(Set<String> userIds) {
        if (userIds.isEmpty()) {
            throw new RuntimeException("Users are empty");
        }

        var phraseBuilder = new StringBuilder("Several humans are late on their code reviews. ");
        var userNamesBuilder = new StringBuilder();
        for (var userId : userIds) {
            var userName = ExtractUserName(userId);
            if (userNamesBuilder.length() != 0) {
                userNamesBuilder.append(", ");
            }
            userNamesBuilder.append(userName);
        }

        return phraseBuilder.append(userNamesBuilder).toString();
    }

    public String GenerateActionItemsOverduePhrase(String userId, ArrayList<ActionItem> overdueActionItems) {
        if (overdueActionItems.isEmpty()) {
            throw new RuntimeException("Overdue items are empty");
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
