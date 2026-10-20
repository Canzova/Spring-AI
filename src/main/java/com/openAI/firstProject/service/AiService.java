package com.openAI.firstProject.service;

import com.openAI.firstProject.dto.ResponseDTO;
import org.springframework.ai.document.Document;
import reactor.core.publisher.Flux;

import java.util.List;

public interface AiService {

    List<ResponseDTO> chat(int count, String field, String sentenceCount);

    Flux<String> streamingChat(String query);

    String memoryChat(String query);

    String usingFastApisWithVectors(String query);
}
