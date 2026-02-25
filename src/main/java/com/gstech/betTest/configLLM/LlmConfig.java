package com.gstech.betTest.configLLM;

import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
public class LlmConfig {

    @Value("${google-ai-gemini.chat-model.api-key}")
    private String apiKey;
    @Value("${google-ai-gemini.chat-model.model-name}")
    private String modelName;

    @Bean
    public GoogleAiGeminiChatModel getChatModel() {

        return GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .build();
    }
}
