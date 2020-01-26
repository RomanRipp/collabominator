package com.roman.ripp.speech;

import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Voice;
import java.beans.PropertyVetoException;
import java.util.Locale;

public class TextToSpeechService {

    Synthesizer mSynthesizer;

    public TextToSpeechService() {
        try {
            System.setProperty(
                    "freetts.voices",
                    "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");

            var modeDescription = new SynthesizerModeDesc(Locale.US);
            mSynthesizer = Central.createSynthesizer(modeDescription);

            mSynthesizer.allocate();
            var engineProperties = mSynthesizer.getSynthesizerProperties();
            engineProperties.setVolume(1);
            engineProperties.setSpeakingRate(140);
        } catch (EngineException | PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    public void Say(String phrase) {
        try {
            mSynthesizer.resume();
            mSynthesizer.speakPlainText(phrase, null);
            mSynthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
        } catch (AudioException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}
