package com.openAI.firstProject.configuration;

import com.openAI.firstProject.advisors.TokenUsageAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AIConfig {

    @Bean(name = "openAiChatClient")
    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel){
        return ChatClient
                .builder(openAiChatModel)
                .defaultAdvisors(new TokenUsageAdvisor(), new SimpleLoggerAdvisor(), new SafeGuardAdvisor(List.of("body")))
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-5-nano")
                        .reasoningEffort("minimal")
//                        .temperature(1.0))
                        .maxCompletionTokens(500))
                .build();
    }
}
