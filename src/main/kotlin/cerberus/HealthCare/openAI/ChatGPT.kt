package cerberus.HealthCare.openAI;

import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ChatGPT(
    @Value("\${openai.api-key}")  // application.yml 또는 환경변수에서 주입됨
    private val apiKey: String
){
    private val client: OpenAI = OpenAI(apiKey)

    suspend fun getCompletionMessage(prompt: String): String {
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-4o-mini"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.User,
                    content = prompt
                )
            )
        )

        val completion: ChatCompletion = client.chatCompletion(chatCompletionRequest)

        return completion.choices.firstOrNull()?.message?.content ?: "failed to get response."
    }

    // java-friendly wrapper
    fun getCompletionMessageBlocking(prompt: String): String =
        kotlinx.coroutines.runBlocking {
            getCompletionMessage(prompt)
        }
}