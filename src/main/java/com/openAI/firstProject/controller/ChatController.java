package com.openAI.firstProject.controller;

import com.openAI.firstProject.dto.ResponseDTO;
import com.openAI.firstProject.service.AiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class ChatController {

    private final AiService aiService;

    public ChatController(AiService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/chat")
    public ResponseEntity<List<ResponseDTO>> chat(@RequestParam(defaultValue = "2", required = false, name = "count") int count,
                                                  @RequestParam(defaultValue = "cricket", required = false, name = "field") String field,
                                                  @RequestParam(defaultValue = "2", required = false, name = "sentenceCount") String sentenceCount) {
        List<ResponseDTO>response = aiService.chat(count, field, sentenceCount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
