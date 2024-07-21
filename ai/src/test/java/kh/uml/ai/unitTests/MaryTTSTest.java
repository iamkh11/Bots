package kh.uml.ai.unitTests;

import java.util.Locale;
import java.util.Set;

import javax.sound.sampled.AudioInputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;

import lombok.extern.log4j.Log4j2;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.util.data.audio.AudioPlayer;

@DirtiesContext
@Log4j2
@ExtendWith(MockitoExtension.class)
class MaryTTSTest {



    @Test
    void SHOULD_() throws Exception{
        
        MaryInterface marytts = new LocalMaryInterface();
        log.info("I currently have " + marytts.getAvailableVoices() + " voices in " + marytts.getAvailableLocales() + " languages available.");
        log.info("Out of these, " + marytts.getAvailableVoices(Locale.US) + " are for US English.");
        Set<String> voices = marytts.getAvailableVoices();
        System.out.println(marytts.getAvailableVoices());
        marytts.setVoice(voices.iterator().next());
        marytts.setLocale(Locale.FRENCH);
        marytts.setVoice("upmc-pierre-hsmm");
        AudioInputStream audio = marytts.generateAudio("Bonjour");
        AudioPlayer player = new AudioPlayer(audio);
        player.start();
        player.join();
      
    }




}
