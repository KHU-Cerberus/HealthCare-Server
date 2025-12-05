package cerberus.HealthCare.logmeal

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.imageio.ImageIO

class LogMealService(private val apiToken: String) {

    companion object {
        private const val BASE_URL = "https://api.logmeal.com/v2"
        private const val MAX_SIZE = 1600
        private const val QUALITY = 0.8f
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    /**
     * 이미지를 인식하고 가장 확률이 높은 음식 이름을 반환합니다.
     */
    fun imageRecognition(imageStream: InputStream): FoodRecognitionResult {
        val url = "$BASE_URL/image/segmentation/complete"

        val compressedImage = compressImage(imageStream)

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "image",
                "image.jpg",
                compressedImage.toRequestBody("image/jpeg".toMediaType())
            )
            .build()

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $apiToken")
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            val responseBody = response.body?.string() ?: "{}"

            if (!response.isSuccessful) {
                println("API 응답 실패 (${response.code}): $responseBody")
                throw Exception("API 호출 실패: ${response.code} - ${response.message}")
            }

            println("API 응답 성공:")
            println(responseBody)

            val jsonResponse = JSONObject(responseBody)

            // imageId 추출
            val imageIdValue = jsonResponse.opt("imageId")
            val imageId = when (imageIdValue) {
                is Number -> imageIdValue.toString()
                is String -> imageIdValue
                else -> null
            }
            println("Image ID: $imageId")

            // segmentation_results 파싱
            val segmentationResults = jsonResponse.optJSONArray("segmentation_results")
            if (segmentationResults == null || segmentationResults.length() == 0) {
                throw Exception("segmentation_results가 비어 있습니다")
            }

            val firstSegment = segmentationResults.getJSONObject(0)
            val recognitionResults = firstSegment.optJSONArray("recognition_results")
            if (recognitionResults == null || recognitionResults.length() == 0) {
                throw Exception("recognition_results가 비어 있습니다")
            }

            // 가장 확률이 높은 음식
            val topFood = recognitionResults.getJSONObject(0)

            println("Top Food JSON: $topFood")

            val foodName = topFood.optString("name", "Unknown")
            val foodId = topFood.optInt("id", -1)
            val probability = topFood.optDouble("prob", 0.0)

            println("파싱 결과 - Name: $foodName, ID: $foodId, Prob: $probability, ImageId: $imageId")

            return FoodRecognitionResult(
                foodName = foodName,
                foodId = foodId,
                probability = probability,
                imageId = imageId
            )
        }
    }

    /**
     * 음식의 영양 정보를 조회합니다.
     */
    fun getNutrition(foodId: Int? = null, imageId: String? = null): NutritionInfo {
        val url = "$BASE_URL/nutrition/recipe/nutritionalInfo"

        val jsonBody = JSONObject().apply {
            if (imageId != null) {
                put("imageId", imageId)
            }
            if (foodId != null && foodId > 0) {
                put("foodId", foodId)
            }
        }

        println("영양정보 요청 Body: $jsonBody")

        val requestBody = jsonBody.toString()
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $apiToken")
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            val responseBody = response.body?.string() ?: "{}"

            if (!response.isSuccessful) {
                println("영양정보 API 응답 실패 (${response.code}): $responseBody")
                throw Exception("영양정보 API 호출 실패: ${response.code} - ${response.message}")
            }

            println("영양정보 API 응답 성공:")
            println(responseBody)

            val jsonResponse = JSONObject(responseBody)
            val nutritionalInfo = jsonResponse.optJSONObject("nutritional_info")
                ?: throw Exception("nutritional_info가 없습니다")

            // totalNutrients 객체에서 실제 영양소 정보 추출
            val totalNutrients = nutritionalInfo.optJSONObject("totalNutrients")
                ?: throw Exception("totalNutrients가 없습니다")

            return NutritionInfo(
                calories = totalNutrients.optJSONObject("ENERC_KCAL")?.optDouble("quantity", 0.0) ?: 0.0,
                totalFat = totalNutrients.optJSONObject("FAT")?.optDouble("quantity", 0.0) ?: 0.0,
                saturatedFat = totalNutrients.optJSONObject("FASAT")?.optDouble("quantity", 0.0) ?: 0.0,
                cholesterol = totalNutrients.optJSONObject("CHOLE")?.optDouble("quantity", 0.0) ?: 0.0,
                sodium = totalNutrients.optJSONObject("NA")?.optDouble("quantity", 0.0) ?: 0.0,
                totalCarbs = totalNutrients.optJSONObject("CHOCDF")?.optDouble("quantity", 0.0) ?: 0.0,
                fiber = totalNutrients.optJSONObject("FIBTG")?.optDouble("quantity", 0.0) ?: 0.0,
                sugar = totalNutrients.optJSONObject("SUGAR")?.optDouble("quantity", 0.0) ?: 0.0,
                protein = totalNutrients.optJSONObject("PROCNT")?.optDouble("quantity", 0.0) ?: 0.0
            )
        }
    }

    /**
     * 이미지를 리사이즈하고 압축합니다.
     */
    private fun compressImage(inputStream: InputStream): ByteArray {
        try {
            val originalImage = ImageIO.read(inputStream)
                ?: throw Exception("이미지를 읽을 수 없습니다")

            val width = originalImage.width
            val height = originalImage.height

            val needsResize = width > MAX_SIZE || height > MAX_SIZE

            val resizedImage = if (needsResize) {
                val scale = if (width > height) {
                    MAX_SIZE.toDouble() / width
                } else {
                    MAX_SIZE.toDouble() / height
                }

                val newWidth = (width * scale).toInt()
                val newHeight = (height * scale).toInt()

                println("이미지 리사이즈: ${width}x${height} -> ${newWidth}x${newHeight}")

                val scaledImage = originalImage.getScaledInstance(
                    newWidth, newHeight, Image.SCALE_SMOOTH
                )

                val bufferedImage = BufferedImage(
                    newWidth, newHeight, BufferedImage.TYPE_INT_RGB
                )
                val graphics = bufferedImage.createGraphics()
                graphics.drawImage(scaledImage, 0, 0, null)
                graphics.dispose()

                bufferedImage
            } else {
                println("이미지 크기 적절: ${width}x${height}")
                val rgbImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
                val graphics = rgbImage.createGraphics()
                graphics.drawImage(originalImage, 0, 0, null)
                graphics.dispose()
                rgbImage
            }

            val outputStream = ByteArrayOutputStream()

            val writers = ImageIO.getImageWritersByFormatName("jpg")
            if (!writers.hasNext()) {
                throw Exception("JPEG writer를 찾을 수 없습니다")
            }

            val writer = writers.next()
            val writeParam = writer.defaultWriteParam
            writeParam.compressionMode = javax.imageio.ImageWriteParam.MODE_EXPLICIT
            writeParam.compressionQuality = QUALITY

            val imageOutputStream = ImageIO.createImageOutputStream(outputStream)
            writer.output = imageOutputStream
            writer.write(null, javax.imageio.IIOImage(resizedImage, null, null), writeParam)

            imageOutputStream.close()
            writer.dispose()

            val compressedBytes = outputStream.toByteArray()
            println("압축된 이미지 크기: ${compressedBytes.size / 1024} KB")

            return compressedBytes

        } catch (e: Exception) {
            println("이미지 압축 중 오류 발생: ${e.message}")
            throw Exception("이미지 압축 실패: ${e.message}", e)
        }
    }
}

/**
 * 음식 인식 결과
 */
data class FoodRecognitionResult(
    val foodName: String,
    val foodId: Int,
    val probability: Double,
    val imageId: String? = null
)

/**
 * 영양 정보
 */
data class NutritionInfo(
    val calories: Double,
    val totalFat: Double,
    val saturatedFat: Double,
    val cholesterol: Double,
    val sodium: Double,
    val totalCarbs: Double,
    val fiber: Double,
    val sugar: Double,
    val protein: Double
)