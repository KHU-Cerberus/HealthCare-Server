package cerberus.HealthCare

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.*
import aws.sdk.kotlin.services.s3.presigners.presignPutObject
import aws.sdk.kotlin.services.s3.presigners.presignGetObject
import aws.smithy.kotlin.runtime.content.ByteStream
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory

@Service
class S3Service {

    private val logger = LoggerFactory.getLogger(S3Service::class.java)
    private val bucketName = "khu-cerberus"
    private val region = "ap-northeast-2"

    private val s3 = S3Client {
        region = this@S3Service.region  // Explicitly set the region
    }

    suspend fun uploadImage(key: String, bytes: ByteStream?, contentType: String) {
        try {
            val request = PutObjectRequest {
                bucket = bucketName
                this.key = key
                body = bytes
                this.contentType = contentType
            }
            s3.putObject(request)
            logger.info("Successfully uploaded image: $key")
        } catch (e: Exception) {
            logger.error("Failed to upload image: $key", e)
            throw e
        }
    }

    fun uploadImageBlocking(key: String, bytes: ByteStream?, contentType: String) {
        runBlocking {
            uploadImage(key, bytes, contentType)
        }
    }

    suspend fun deleteImage(key: String) {
        try {
            val request = DeleteObjectRequest {
                bucket = bucketName
                this.key = key
            }
            s3.deleteObject(request)
            logger.info("Successfully deleted image: $key")
        } catch (e: Exception) {
            logger.error("Failed to delete image: $key", e)
            throw e
        }
    }

    fun deleteImageBlocking(key: String) {
        runBlocking {
            deleteImage(key)
        }
    }

    suspend fun getPresignedUrl(
        key: String,
        expireSeconds: Long = 3600
    ): String {
        return try {
            val req = GetObjectRequest {
                bucket = bucketName
                this.key = key
            }
            val presigned = s3.presignGetObject(req, expireSeconds.seconds)
            presigned.url.toString()
        } catch (e: Exception) {
            logger.error("Failed to generate download presigned URL for: $key", e)
            throw e
        }
    }

    fun getPresignedUrlBlocking(key: String, expireSeconds: Long = 3600): String {
        return runBlocking {
            getPresignedUrl(key, expireSeconds)
        }
    }
}