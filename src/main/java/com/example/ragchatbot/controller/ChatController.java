package com.example.ragchatbot.controller;

import com.example.ragchatbot.service.ChatService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @PostMapping("/embed")
    public String embedDocument() {
        // Hardcoded path for now; can make configurable
        String filePath = System.getProperty("user.home") + "/Documents/WEB past present future 2014.pdf";
        return chatService.embedPdf(filePath);
    }

    @PostMapping("/ask")
    public String askQuestion(@RequestParam String question) {
        return chatService.askQuestion(question);
    }
}
