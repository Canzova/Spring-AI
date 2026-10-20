package com.openAI.firstProject.controller;

import com.openAI.firstProject.dto.ResponseDTO;
import com.openAI.firstProject.service.AiService;
import com.openAI.firstProject.service.VectorService;
import org.springframework.ai.document.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping
public class ChatController {

    private final AiService aiService;
    private final VectorService vectorService;

    public ChatController(AiService aiService, VectorService vectorService) {
        this.aiService = aiService;
        this.vectorService = vectorService;
    }

    @GetMapping(value = "/chat")
    public ResponseEntity<List<ResponseDTO>> chat(@RequestParam(defaultValue = "2", required = false, name = "count") int count,
                                                  @RequestParam(defaultValue = "cricket", required = false, name = "field") String field,
                                                  @RequestParam(defaultValue = "2", required = false, name = "sentenceCount") String sentenceCount) {
        List<ResponseDTO>response = aiService.chat(count, field, sentenceCount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/stream-chat")
    public ResponseEntity<Flux<String>>streamingChat(@RequestParam(name = "query", required = false, defaultValue = "tell me about 7 wonders of India") String query
    ){
        return new ResponseEntity<>(aiService.streamingChat(query), HttpStatus.OK);
    }

    @GetMapping("/memory-chat")
    public ResponseEntity<String>memoryChat(@RequestParam(name = "query", required = false, defaultValue = "tell me about 7 wonders of India") String query
    ){
        return new ResponseEntity<>(aiService.memoryChat(query), HttpStatus.OK);
    }

    @PostMapping("/vector/saveData")
    public ResponseEntity<Void> saveDataToVector(){
        vectorService.saveDocument();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/vector/getInfo")
    public ResponseEntity<String>getInfo(@RequestParam(name = "query", defaultValue = "Summary of Nihal in 20 words", required = false) String query){
        return new ResponseEntity<>(aiService.usingFastApisWithVectors(query), HttpStatus.OK);
    }
}
