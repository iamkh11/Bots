package kh.uml.ai.test;

import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaApi.ChatRequest;
import org.springframework.ai.ollama.api.OllamaApi.ChatResponse;
import org.springframework.ai.ollama.api.OllamaApi.Message;
import org.springframework.ai.ollama.api.OllamaApi.Message.Role;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.log4j.Log4j2;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.util.data.audio.AudioPlayer;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.sound.sampled.AudioInputStream;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.EngineStateError;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;

@Log4j2
@Controller
@RequestMapping("/")
public class WebController {

    // Singleton instance of Synthesizer
    private static Synthesizer synthesizer = null;

    private final OllamaChatModel ollamaChatModel;

    public WebController(OllamaChatModel ollamaChatModel) {
        this.ollamaChatModel=ollamaChatModel;
    }



    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("textForm", new TextForm());
        return "index";
    }


    // @PostMapping("/speak")
    // public String speak(@ModelAttribute TextForm textForm, Model model, RedirectAttributes redirectAttributes) throws EngineException, EngineStateError {
        
    //     /*
    //      * mary.setLocale(Locale.FRENCH);
    //         mary.setVoice("upmc-pierre-hsmm");
    //      */
    //     try {
    //         final LlamaResponse aiResponse = llamaAiService.generateMessage(textForm.getText());
    //         log.info(aiResponse);
    //         if (synthesizer == null) {
    //             // VOICES 
    //             // Kevin
    //             System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
    //             Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
    //             synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
    //         }
    //         // Check if synthesizer is allocated
    //         if (synthesizer.getEngineState() != Synthesizer.ALLOCATED) {
    //             synthesizer.allocate();
    //         }
    //         synthesizer.resume();
    //         synthesizer.speakPlainText(aiResponse.getMessage(), null);
    //         synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);

    //     } catch (Exception e) {
    //         log.info(e.getMessage());
    //         e.printStackTrace();
    //     } 

    //     model.addAttribute("textForm", new TextForm()); // Clear the form

    //     // Add flash attributes
    //     redirectAttributes.addFlashAttribute("message", "Text has been spoken!");

    //     // Redirect to the same page to refresh
    //     return "redirect:/";
    // }



    @PostMapping("/speak")
    public String speak(@ModelAttribute TextForm textForm, Model model, RedirectAttributes redirectAttributes) throws InterruptedException{
        try {

            var chatMemory = new InMemoryChatMemory();
            var memoryAdvisor = new MessageChatMemoryAdvisor(chatMemory);
            
            var ollamaApi = new OllamaApi();
            // var chatModel = new OllamaChatModel(ollamaApi,
            //             OllamaOptions.create()
            //                 .withModel(OllamaOptions.DEFAULT_MODEL)
            //                 .withTemperature(0.9f));
            // // Sync request
            var request = ChatRequest.builder("llama3")
                .withStream(false)
                .withMessages(List.of(
                        Message.builder(Role.SYSTEM)
                        .withContent("Vous êtes un bot d'appel qui reçoit des appels des patients du docteur Souhayl. Votre rôle est de gérer et d'interagir avec les patients du docteur Souhayl en langue française.")
                            .build(),
                        Message.builder(Role.USER)
                            .withContent(textForm.getText())
                            .build()))
                .withOptions(OllamaOptions.create().withTemperature(0.9f).withModel("llama3"))
                
                .build();

                
            ChatResponse response = ollamaApi.chat(request);
            log.info(response.message().content());

            // Initialize MaryTTS
            MaryInterface marytts = new marytts.LocalMaryInterface();
            marytts.setLocale(Locale.FRENCH);
            // Set<String> availableVoices = marytts.getAvailableVoices();
            // log.info("Available voices: " + availableVoices);
            
            marytts.setVoice("upmc-pierre-hsmm");
    
            // Generate audio
            AudioInputStream audio = marytts.generateAudio(response.message().content());
    
            // Play the audio
            AudioPlayer player = new AudioPlayer();
            player.setAudio(audio);
            player.start();
    
            // Wait for the audio to finish playing
            player.join();
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
    
    
        model.addAttribute("textForm", new TextForm()); // Clear the form
    
        // Add flash attributes
        redirectAttributes.addFlashAttribute("message", "Text has been spoken!");
    
        // Redirect to the same page to refresh
        return "redirect:/";
    }
    





}
