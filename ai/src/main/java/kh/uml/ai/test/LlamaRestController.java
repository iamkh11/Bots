package kh.uml.ai.test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
public class LlamaRestController {

    // private final LlamaAiService llamaAiService;

    // public LlamaRestController(LlamaAiService llamaAiService) {
    //     this.llamaAiService = llamaAiService;
    // }

    // @GetMapping("api/v1/ai/generate")
    // public ResponseEntity<LlamaResponse> generate(
    //         @RequestParam(value = "promptMessage", defaultValue = "can you define spring boot java apps") String promptMessage) {
    //     final LlamaResponse aiResponse = llamaAiService.generateMessage(promptMessage);
    //     log.info(aiResponse.getMessage());
    //     return ResponseEntity.status(HttpStatus.OK).body(aiResponse);
    // }

    // @GetMapping("api/v1/ai/generate/joke/{topic}")
    // public ResponseEntity<LlamaResponse> generateJoke(@PathVariable String topic) {
    //     final LlamaResponse aiResponse = llamaAiService.generateJoke(topic);
    //     return ResponseEntity.status(HttpStatus.OK).body(aiResponse);
    // }

}