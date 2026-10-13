package com.openAI.firstProject.service;

import com.openAI.firstProject.dto.ResponseDTO;

import java.util.List;

public interface AiService {

    List<ResponseDTO> chat(int count, String field, String sentenceCount);
}
