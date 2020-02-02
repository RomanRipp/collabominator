package com.roman.ripp.speech;

import com.roman.ripp.collab.ActionItem;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.roman.ripp.speech.RandomizedPhraseGenerator.ExtractUserName;
import static org.junit.jupiter.api.Assertions.*;

class RandomizedPhraseGeneratorTest {

    @Test
    void generateSingleActionItemOverduePhrase() {
        var generator = new RandomizedPhraseGenerator();
        var overdueActionItems = new ArrayList<ActionItem>() {{ add(new ActionItem()); }};
        var phrase = generator.GenerateActionItemsOverduePhrase("Adolf.Hitler@nazi_germany.com", overdueActionItems);
        assertTrue(phrase.contains("Adolf Hitler is late on his code review"));
    }

    @Test
    void generateMultipleActionItemOverduePhrase() {
        var generator = new RandomizedPhraseGenerator();
        var overdueActionItems = new ArrayList<ActionItem>() {{
            add(new ActionItem());
            add(new ActionItem());
        }};
        var phrase = generator.GenerateActionItemsOverduePhrase("Adolf.Hitler@nazi_germany.com", overdueActionItems);
        assertTrue(phrase.contains("Adolf Hitler is late on his code reviews"));
    }

    @Test
    void extractUserName() {
        assertEquals("Adolf Hitler", ExtractUserName("Adolf.Hitler"));
        assertEquals("Adolf Hitler", ExtractUserName("Adolf.Hitler@nazi_germany.com"));
        assertEquals("Adolf Hitler", ExtractUserName("Adolf Hitler"));
        assertEquals("Adolf Hitler", ExtractUserName(" Adolf  Hitler "));
        assertEquals("Adolf Hitler", ExtractUserName("Adolf?Hitler?"));
    }
}