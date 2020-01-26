package com.roman.ripp.speech;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TextToSpeechServiceTest {

    @Test
    void sayTest() {
        var service = new TextToSpeechService();
        service.Say("Roses are red, violets are blue, Adolf Hitler is late on his code review");
        assertTrue(true);
    }
}