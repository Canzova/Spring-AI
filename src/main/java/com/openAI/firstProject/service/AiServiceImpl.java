package com.openAI.firstProject.service;

import com.openAI.firstProject.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;
    private final VectorService vectorService;
    private final VectorStore vectorStore;

    @Value("classpath:/prompts/systemPrompt.st")
    private Resource systemPrompt;

    @Value("classpath:/prompts/userPrompt.st")
    private Resource userPrompt;

    @Override
    public List<ResponseDTO> chat(int count, String field, String sentenceCount) {
//        return usingPromptTemplates(count, field, sentenceCount);
        return usingFastApis(count, field, sentenceCount);
//        return usingPromptFiles(count, field, sentenceCount);

    }

    @Override
    public Flux<String> streamingChat(String query) {
        return chatClient.prompt(query).stream().content();
    }

    @Override
    public String memoryChat(String query) {
        // Here i am passing the conv Id as "abc" but in real system con id is determined form userid and session
        return chatClient.prompt(query).advisors(a -> a.param(ChatMemory.CONVERSATION_ID, "abc")).call().content();
    }


    private List<ResponseDTO> usingPromptFiles(int count, String field, String sentenceCount) {
        PromptTemplate userPromptTemplate = new PromptTemplate(userPrompt);
        PromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemPrompt);

        Message userMessage = userPromptTemplate.createMessage(Map.of("count", count, "field", field));
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("sentenceCount", sentenceCount, "field", field));

        Prompt prompt = new Prompt(userMessage, systemMessage);

        return chatClient
                .prompt(prompt)
                .call()
                .entity(new ParameterizedTypeReference<List<ResponseDTO>>() {
                });
    }

    private List<ResponseDTO> usingFastApis(int count, String field, String sentenceCount) {
        return chatClient
                .prompt()
                .user(userMessage -> userMessage.text("Tell me top {count} achievers in filed of {field}")
                        .params(Map.of("count", count, "field", field )))
                .system(systemMessage -> systemMessage.text("You are an expert in filed of {field}, answer only in {sentenceCount} sentences")
                        .params(Map.of("sentenceCount", sentenceCount, "field", field)))
//                .advisors(new SimpleLoggerAdvisor(), new SafeGuardAdvisor(List.of("body")))
                .call()
                .entity(new ParameterizedTypeReference<List<ResponseDTO>>() {
                });
    }

    @Override
    public String usingFastApisWithVectors(String query) {

        return chatClient
                .prompt()
                .user(userMessage -> userMessage.text(query))
                .advisors(QuestionAnswerAdvisor
                        .builder(vectorStore)
                        .searchRequest(SearchRequest
                                .builder()
                                .topK(3)
                                .similarityThreshold(0.6)
                                .build())
                        .build())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, "abc"))
//                                .advisors(new SimpleLoggerAdvisor(), new SafeGuardAdvisor(List.of("body")))
                .call()
                .content();
    }

    private List<ResponseDTO> usingPromptTemplates(int count, String field, String sentenceCount) {
        PromptTemplate userPromptTemplate = new PromptTemplate("Tell me top {count} achievers in filed of {field}");
        PromptTemplate systemPromptTemplate = new SystemPromptTemplate("You are an expert in filed of {field}, answer only in {sentenceCount} sentences");

        Message userMessage = userPromptTemplate.createMessage(Map.of("count", count, "field", field));
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("sentenceCount", sentenceCount, "field", field));

        Prompt prompt = new Prompt(userMessage, systemMessage);

        return chatClient
                .prompt(prompt)
                .call()
                .entity(new ParameterizedTypeReference<List<ResponseDTO>>() {
                });
    }
}
