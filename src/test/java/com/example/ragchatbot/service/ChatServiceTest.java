package com.example.ragchatbot.service;

import com.example.ragchatbot.exception.DocumentNotFoundException;
import com.example.ragchatbot.exception.InvalidDocumentException;
import com.example.ragchatbot.exception.ProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ai.document.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class ChatServiceTest {

    private static final String TEST_PDF_PATH = "/valid/path.pdf";
private static final String NON_EXISTENT_PDF_PATH = "/non/existent/path.pdf";

    @MockBean
    private VectorStore vectorStore;

    @MockBean
    private OllamaChatModel chatModel;

    @Autowired
    private ChatService chatService;

    @Test
    void embedPdf_WhenFilePathIsNull_ShouldThrowInvalidDocumentException() {
        assertThrows(InvalidDocumentException.class, () -> chatService.embedPdf(null));
    }

    @Test
    void embedPdf_WhenFilePathIsEmpty_ShouldThrowInvalidDocumentException() {
        assertThrows(InvalidDocumentException.class, () -> chatService.embedPdf(""));
    }

    @Test
    void embedPdf_WhenFileDoesNotExist_ShouldThrowDocumentNotFoundException() throws IOException {
        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.exists(Paths.get(NON_EXISTENT_PDF_PATH))).thenReturn(false);
            assertThrows(DocumentNotFoundException.class, () -> chatService.embedPdf(NON_EXISTENT_PDF_PATH));
        }
    }

    @Test
    void embedPdf_WhenPdfExtractionFails_ShouldThrowProcessingException() throws IOException {
        String validPath = TEST_PDF_PATH;
        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.exists(Paths.get(validPath))).thenReturn(true);
            filesMock.when(() -> Files.newInputStream(Paths.get(validPath))).thenThrow(new IOException("PDF extraction failed"));
            assertThrows(ProcessingException.class, () -> chatService.embedPdf(validPath));
        }
    }

    @Test
    void askQuestion_WhenQuestionIsNull_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> chatService.askQuestion(null));
    }

    @Test
    void askQuestion_WhenQuestionIsEmpty_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> chatService.askQuestion(""));
    }

    @Test
    void askQuestion_WhenNoDocumentsFound_ShouldThrowDocumentNotFoundException() {
        String question = "test question";
        when(vectorStore.similaritySearch(any(SearchRequest.class))).thenReturn(List.of());
        assertThrows(DocumentNotFoundException.class, () -> chatService.askQuestion(question));
    }

    @Test
    void askQuestion_WhenProcessingFails_ShouldThrowProcessingException() {
        String question = "test question";
        Document mockDoc = mock(Document.class);
        when(vectorStore.similaritySearch(any(SearchRequest.class))).thenReturn(List.of(mockDoc));
        when(chatModel.call(anyString())).thenThrow(new RuntimeException("Processing failed"));
        assertThrows(ProcessingException.class, () -> chatService.askQuestion(question));
    }
}