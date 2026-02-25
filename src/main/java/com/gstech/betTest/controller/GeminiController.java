package com.gstech.betTest.controller;

import com.gstech.betTest.dto.RequestDTO;
import com.gstech.betTest.dto.ResponseDTO;
import com.gstech.betTest.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GeminiController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/chat")
    public ResponseEntity<ResponseDTO> getGeminiChat(@RequestBody RequestDTO request) {
        String response = chatService.getResponse(request.message());
        return ResponseEntity.ok(new ResponseDTO(response));
    }
}
