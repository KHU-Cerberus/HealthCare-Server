//package cerberus.HealthCare.openAI
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import okhttp3.MediaType.Companion.toMediaType
//import okhttp3.Request
//import okhttp3.RequestBody.Companion.toRequestBody
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.stereotype.Service
//
//@Service
//class OpenAIService(
//    private val client: okhttp3.OkHttpClient,
//    private val mapper: ObjectMapper,
//    @Value("\${openai.api.key}") private val apiKey: String
//) {
//
//    private val mediaType = "application/json; charset=utf-8".toMediaType()
//    private val apiURL = "https://api.openai.com/v1/chat/completions"
//
//    fun chat(prompt: String, model: String = "gpt-4o-mini"): String {
//        val payload = mapOf(
//            "model" to model,
//            "messages" to listOf(
//                mapOf("role" to "user", "content" to prompt)
//            )
//        )
//
//        val json = mapper.writeValueAsString(payload)
//
//        val request = Request.Builder()
//            .url(apiURL)
//            .header("Authorization", "Bearer $apiKey")
//            .post(json.toRequestBody(mediaType))
//            .build()
//
//        client.newCall(request).execute().use { response ->
//            if (!response.isSuccessful) throw RuntimeException("OpenAI API Error: ${response.code}")
//
//            val body = response.body?.string() ?: throw RuntimeException("Empty response")
//            val tree = mapper.readTree(body)
//
//            return tree["choices"][0]["message"]["content"].asText()
//        }
//    }
//}
