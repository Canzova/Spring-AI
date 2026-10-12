package com.openAI.firstProject.controller;

import com.google.genai.Chat;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ChatController {

    private ChatClient openAiChatClient;
    private ChatClient googleGenAiChatModel;

    public ChatController(@Qualifier("OpenAiChatClient") ChatClient OpenAiChatClient, @Qualifier("GoogleGenAiChatClient") ChatClient googleGenAiChatModel) {
        this.openAiChatClient = OpenAiChatClient;
        this.googleGenAiChatModel = googleGenAiChatModel;
    }


    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam(value = "prompt", required = false) String prompt){
        String response = googleGenAiChatModel.prompt(prompt).call().content();
        return ResponseEntity.ok(response);
    }
}
