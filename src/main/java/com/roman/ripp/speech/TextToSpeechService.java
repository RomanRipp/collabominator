package com.roman.ripp.speech;

import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class TextToSpeechService {

    TextToSpeechClient mTtsClient;
    AudioConfig mAudioConfig;
    VoiceSelectionParams mVoice;

    public TextToSpeechService() {
        try {
            mTtsClient = TextToSpeechClient.create();
            mVoice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("en-GB")
                    .setSsmlGender(SsmlVoiceGender.MALE)
                    .setName("en-GB-Standard-B")
                    .build();
            mAudioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Say(String phrase) {
        try {
            var input = SynthesisInput.newBuilder()
                    .setText(phrase)
                    .build();

            var response = mTtsClient.synthesizeSpeech(input, mVoice,
                    mAudioConfig);

            var audioContents = response.getAudioContent();

            var targetStream = new ByteArrayInputStream(audioContents.toByteArray());

            Player playMP3;
            playMP3 = new Player(targetStream);
            playMP3.play();

        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }


}
