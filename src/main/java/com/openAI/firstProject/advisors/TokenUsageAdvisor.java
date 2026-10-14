package com.openAI.firstProject.advisors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.metadata.Usage;
import reactor.core.publisher.Flux;

@Slf4j
public class TokenUsageAdvisor implements CallAdvisor, StreamAdvisor {
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        log.info("TokenUsageAdvisor started");

        ChatClientResponse response = callAdvisorChain.nextCall(chatClientRequest);

        Usage usage = response.chatResponse().getMetadata().getUsage();

        log.info("Input tokens use : {}", usage.getPromptTokens());
        log.info("Output tokens use : {}", usage.getCompletionTokens());
        log.info("Total tokens use : {}", usage.getTotalTokens());

        log.info("TokenUsageAdvisor ended");

        return response;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        return null;
    }

    @Override
    public String getName() {
        return "TokenUsageAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
