package com.openAI.firstProject.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Bean(name = "OpenAiChatClient")
    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel){
        return ChatClient.builder(openAiChatModel).build();
    }

    @Bean(name = "GoogleGenAiChatClient")
    public ChatClient geminiChatClient(GoogleGenAiChatModel googleGenAiChatModel){
        return ChatClient.builder(googleGenAiChatModel).build();
    }


}
