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
    public ResponseEntity<List<ResponseDTO>> chat(@RequestParam(value = "prompt", required = false) String prompt){
        List<ResponseDTO>response = aiService.chat(prompt);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
