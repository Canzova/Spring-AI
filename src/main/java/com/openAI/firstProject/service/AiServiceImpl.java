package com.openAI.firstProject.service;

import com.openAI.firstProject.dto.ResponseDTO;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;

    public AiServiceImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public List<ResponseDTO> chat(String query) {
//        chat2(query);
        return chatClient
                .prompt()
                .system("Answer in short and crips way")
                .user(query)
                .call()
                .entity(new ParameterizedTypeReference<List<ResponseDTO>>() {});
    }
//
//    public void chat2(String query) {
//        ChatResponse chatResponse = chatClient
//                .prompt()
//                .system("Answer in short and crips way")
//                .user(query)
//                .call()
//                .chatResponse();
//
//        String res = chatResponse.getResult().getOutput().getText();
//        System.out.println(res);
//
//         ChatResponseMetadata metadata = chatResponse.getMetadata();
//         Usage usage = metadata.getUsage();
//
//         System.out.println(usage.getPromptTokens());
//         System.out.println(usage.getTotalTokens());
//         System.out.println(usage.getCompletionTokens());
//
//        System.out.println(metadata.getModel());
//        System.out.println(metadata.getPromptMetadata());
//    }

}
