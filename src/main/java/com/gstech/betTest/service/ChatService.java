package com.gstech.betTest.service;

import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    @Autowired
    private GoogleAiGeminiChatModel chatModel; // Injeta o Bean aqui

    public String getResponse(String userMessage) {
        try {
            // Aqui você poderia adicionar lógica extra, como logs ou filtros
            return chatModel.chat(userMessage);
        } catch (Exception e) {
            return "Desculpe, estou com dificuldades de conexão agora.";
        }
    }
}


