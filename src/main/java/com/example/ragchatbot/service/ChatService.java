package com.example.ragchatbot.service;

import org.springframework.stereotype.Service;

import com.example.ragchatbot.exception.DocumentNotFoundException;
import com.example.ragchatbot.exception.InvalidDocumentException;
import com.example.ragchatbot.exception.ProcessingException;

import lombok.Value;

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

    private static final int CHUNK_SIZE = 1000;
    private static final int DEFAULT_TOP_K = 3;
   
    private VectorStore vectorStore;
  
    private OllamaChatModel chatModel;

    public ChatService(VectorStore vectorStore, OllamaChatModel chatModel) {
        this.vectorStore = vectorStore;
        this.chatModel = chatModel;
    }

    // 1. Embed PDF
    public String embedPdf(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new InvalidDocumentException("File path cannot be null or empty");
        }

        try {
            if (!Files.exists(Paths.get(filePath))) {
                throw new DocumentNotFoundException("Document not found at path: " + filePath);
            }

            String text = extractTextFromPdf(filePath);
            // Split text into chunks (for demo, simple split)
           
            for (int i = 0; i < text.length(); i += CHUNK_SIZE) {
                String chunk = text.substring(i, Math.min(i + CHUNK_SIZE, text.length()));
                vectorStore.add(List.of(new Document(chunk)));
            }
            return "Document embedded to Chroma!";
        } catch (IOException e) {
            throw new ProcessingException("Failed to extract text from PDF: " + e.getMessage());
        }
    }

    // 2. Answer question
    public String askQuestion(String question) {
        if (question == null || question.isEmpty()) {
            throw new IllegalArgumentException("Question cannot be null or empty");
        }

        List<Document> results = searchDocuments(question);

        try {
            StringBuilder context = new StringBuilder();
            for (Document doc : results) {
                context.append(doc.getFormattedContent()).append("\n---\n");
            }
            String prompt = "Context:\n" + context + "\n\nQuestion: " + question;
            return chatModel.call(prompt);
        } catch (Exception e) {
            throw new ProcessingException("Failed to process question: " + e.getMessage());
        }
    }

    private List<Document> searchDocuments(String question) {
        SearchRequest searchRequest = SearchRequest.builder()
            .query(question)
            .topK(DEFAULT_TOP_K)
            .build();

        List<Document> results = vectorStore.similaritySearch(searchRequest);

        if (results == null || results.isEmpty()) {
            throw new DocumentNotFoundException("No relevant documents found for the question");
        }
        return results;
    }

    private String extractTextFromPdf(String filePath) throws IOException {
        try (PDDocument document = PDDocument.load(Files.newInputStream(Paths.get(filePath)))) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
}
