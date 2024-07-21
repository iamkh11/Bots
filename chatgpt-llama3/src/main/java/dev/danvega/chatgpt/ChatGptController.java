package dev.danvega.chatgpt;

import java.util.Locale;

import javax.sound.sampled.AudioInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.aop.config.AdviceEntry;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import marytts.MaryInterface;
import marytts.util.data.audio.AudioPlayer;

@Controller
@CrossOrigin
public class ChatGptController {

    private static final Logger log = LoggerFactory.getLogger(ChatGptController.class);
    private final ChatClient chatClient;
    private AudioPlayer player;

    public ChatGptController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .build();
    }

    @GetMapping("")
    public String home() {
        return "index";
    }

    @HxRequest
    @PostMapping("/api/chat")
    public HtmxResponse generate(@RequestParam String message, Model model) throws Exception {
        log.info("User Message: {}", message);
        String response = chatClient
                .prompt()
                .user(message)
                .call()
                .content();

        model.addAttribute("response", response);
        model.addAttribute("message", message);
        log.info("LLAMA3 Message: {}", response);
        try {
            MaryInterface marytts = new marytts.LocalMaryInterface();
            marytts.setLocale(Locale.FRENCH);
            marytts.setVoice("upmc-pierre-hsmm");

            AudioInputStream audio = marytts.generateAudio(response);

            // Play the audio
            if (player != null && player.isAlive()) {
                player.cancel();
            }
            player = new AudioPlayer();
            player.setAudio(audio);
            player.start();
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
        
        return HtmxResponse.builder()
                .view("response :: responseFragment")
                .view("recent-message-list :: messageFragment")
                .build();
    }

    @PostMapping("/api/play")
    public ResponseEntity<Void> playAudio(@RequestBody String message) {
        log.info("HELLO {}", message);
        if (player != null && player.isAlive()) {
            player.cancel();
        }
        // Create a new instance of AudioPlayer
        player = new AudioPlayer();
        try {
            MaryInterface marytts = new marytts.LocalMaryInterface();
            marytts.setLocale(Locale.FRENCH);
            marytts.setVoice("upmc-pierre-hsmm");
    
            AudioInputStream audio = marytts.generateAudio(message);
            player.setAudio(audio);
            player.start();
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }
    
    

    @PostMapping("/api/stop")
    public ResponseEntity<Void> stopAudio() {
        if (player != null && player.isAlive()) {
            player.cancel();
        }
        return ResponseEntity.ok().build();
    }

}
