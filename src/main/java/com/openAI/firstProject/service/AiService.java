package com.openAI.firstProject.service;

import com.openAI.firstProject.dto.ResponseDTO;

import java.util.List;

public interface AiService {

    public List<ResponseDTO> chat(String query);

}
