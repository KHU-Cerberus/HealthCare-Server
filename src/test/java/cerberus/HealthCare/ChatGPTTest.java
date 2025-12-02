package cerberus.HealthCare;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class ChatGPTTest {

    private ChatGPT chat;
    private static final String API_KEY = "API_KEY";

    @BeforeEach
    public void setUp() {
        chat = new ChatGPT(API_KEY);
    }

    @Test
    public void testGetCompletionMessageBlocking() {
        // Given
        String prompt = "say 'this is test'";

        // When
        String result = chat.getCompletionMessageBlocking(prompt);

        // Then
        assertNotNull(result, "Result should not be null");
        assertFalse(result.isEmpty(), "Result should not be empty");
        System.out.println("Response: " + result);

        // Optional: Check if response contains expected text
        assertTrue(result.toLowerCase().contains("test"),
                "Response should contain the word 'test'");
    }
}