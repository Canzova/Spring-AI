package com.openAI.firstProject.service;

import com.ethlo.time.DateTime;
import com.openAI.firstProject.dto.ResponseDTO;
import com.openAI.firstProject.tools.DateTimeTool;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.preretrieval.query.transformation.CompressionQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
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

    @Value("classpath:/prompts/multiQueryExpander.st")
    private Resource multiQueryExpanderPrompt;

    @Override
    public List<ResponseDTO> chat(int count, String field, String sentenceCount) {
//        return usingPromptTemplates(count, field, sentenceCount);
        return usingFastApis(count, field, sentenceCount);
//        return usingPromptFiles(count, field, sentenceCount);

    }

    @Override
    public Flux<String> streamingChat(String query) {
        return chatClient
                .prompt(query)
                .tools(new DateTimeTool())
                .stream()
                .content();
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

        var advisor = RetrievalAugmentationAdvisor.builder()
                // Pre-retrival
                .queryTransformers(
                        // Compress the user-hostory and prompt
                        CompressionQueryTransformer.builder()
                                .chatClientBuilder(chatClient.mutate().clone())
                                .build(),

                        // Rewrite the query to make it more effecient
                        RewriteQueryTransformer.builder()
                                .chatClientBuilder(chatClient.mutate().clone())
                                .build(),

                        // Translate it into english is the prompt is in another language
                        TranslationQueryTransformer.builder()
                                .chatClientBuilder(chatClient.mutate().clone())
                                .targetLanguage("english")
                                .build()
                )

                .queryExpander(
                        MultiQueryExpander.builder()
                                .chatClientBuilder(chatClient.mutate().defaultAdvisors(List.of()))
                                .includeOriginal(true)
                                .numberOfQueries(3)
                                .promptTemplate(new PromptTemplate(multiQueryExpanderPrompt))
                                .build()
                )

                //Retrival
                .documentRetriever(
                        // Retrive the documents form the resouce or vector db
                        VectorStoreDocumentRetriever.builder()
                                .topK(3)
                                .similarityThreshold(0.4)
                                .vectorStore(vectorStore)
                                .build()
                )
                // You can also use document joiner

                // Post- retrival ---> .documentPostProcessor you have to create an implementation of it

                .build();

        return chatClient
                .prompt()
                .user(userMessage -> userMessage.text(query))
                .advisors(advisor)
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
