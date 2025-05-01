package com.example.ragchatbot.controller;

import com.example.ragchatbot.service.ChatService;

import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {

    private ChatService chatService;

    @Value("${app.embed.file-path}")
    private String filePath;

    @PostMapping("/embed")
    public String embedDocument() {
        return chatService.embedPdf(filePath);
    }

    @PostMapping("/ask")
    public String askQuestion(@RequestParam @NotNull String question) {
        return chatService.askQuestion(question);
    }
}
