package com.openAI.firstProject.controller;

import com.openAI.firstProject.dto.ResponseDTO;
import com.openAI.firstProject.service.AiService;
import okhttp3.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping
public class ChatController {

    private final AiService aiService;

    public ChatController(AiService aiService) {
        this.aiService = aiService;
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
}
