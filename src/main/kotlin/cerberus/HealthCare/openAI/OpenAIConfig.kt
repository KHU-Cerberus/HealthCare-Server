//@Service
//class ChatGptClient(
//    private val webClient: WebClient
//) {
//
//    @Value("\${openai.api.key}")
//    lateinit var apiKey: String
//
//    suspend fun generate(prompt: String): String {
//        return webClient.post()
//            .uri("https://api.openai.com/v1/chat/completions")
//            .header("Authorization", "Bearer $apiKey")
//            .bodyValue(mapOf(
//                "model" to "gpt-4o",
//                "messages" to listOf(
//                    mapOf("role" to "user", "content" to prompt)
//                )
//            ))
//            .retrieve()
//            .bodyToMono(ChatResponse::class.java)
//            .awaitSingle()
//            .choices[0].message.content
//    }
//}
//
//data class ChatResponse(
//    val choices: List<Choice>
//)
//
//data class Choice(
//    val message: Message
//)
//
//data class Message(
//    val role: String,
//    val content: String
//)