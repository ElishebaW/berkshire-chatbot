package com.example.ragchatbot.service;

import org.springframework.stereotype.Service;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.document.Document;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;

import org.springframework.ai.vectorstore.SearchRequest;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

@Service
public class ChatService {
   
    private VectorStore vectorStore;
  
    private OllamaChatModel chatModel;

    public ChatService(VectorStore vectorStore, OllamaChatModel chatModel) {
        this.vectorStore = vectorStore;
        this.chatModel = chatModel;
    }

    // 1. Embed PDF
    public String embedPdf(String filePath) {
        try {
            String text = extractTextFromPdf(filePath);
            // Split text into chunks (for demo, simple split)
            int chunkSize = 1000;
            for (int i = 0; i < text.length(); i += chunkSize) {
                String chunk = text.substring(i, Math.min(i + chunkSize, text.length()));
                vectorStore.add(List.of(new Document(chunk)));
            }
            return "Document embedded to Chroma!";
        } catch (IOException e) {
            return "Failed to read PDF: " + e.getMessage();
        }
    }

    // 2. Answer question
    public String askQuestion(String question) {
        // Retrieve top chunks from Chroma
        List<Document> results = vectorStore.similaritySearch(SearchRequest.builder().query(question).topK(3).build());
        StringBuilder context = new StringBuilder();
        for (Document doc : results) {
            context.append(doc.getFormattedContent()).append("\n---\n");
        }
        String prompt = "Context:\n" + context + "\n\nQuestion: " + question;
        return chatModel.call(prompt);
    }

    // Helper: Extract text from PDF
    private String extractTextFromPdf(String filePath) throws IOException {
        try (PDDocument document = PDDocument.load(Files.newInputStream(Paths.get(filePath)))) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
}
