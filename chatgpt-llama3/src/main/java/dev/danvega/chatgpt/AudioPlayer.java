package dev.danvega.chatgpt;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioPlayer extends Thread {
    private AudioInputStream audio;

    public void setAudio(AudioInputStream audio) {
        this.audio = audio;
    }

    @Override
    public void run() {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
            clip.drain();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopAudio() {
        if (audio != null) {
            try {
                Clip clip = AudioSystem.getClip();
                clip.open(audio);
                clip.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
