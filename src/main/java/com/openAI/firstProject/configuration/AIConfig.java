package com.openAI.firstProject.configuration;

import com.openAI.firstProject.advisors.TokenUsageAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AIConfig {

    @Bean
    public ChatMemory chatClient(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .maxMessages(10)
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .build();
    }

    /*
        By default, spring AI gives us ChatMemory implementation : MessageWindowChatMemory and chatMemoryRepository impl : InMemoryChatMemoryRepository
        So here we can directly use the bean of ChatMemory. and Internally it will be using default impl of chatMemoryRepository
     */

    @Bean(name = "openAiChatClient")
    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel, ChatMemory chatMemory) {
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
