//package cerberus.HealthCare.aws.service
//
//import org.springframework.stereotype.Service
//import software.amazon.awssdk.core.sync.RequestBody
//import software.amazon.awssdk.services.s3.S3Client
//
//@Service
//class S3Service(
//    private val s3Client: S3Client
//) {
//
//    fun uploadImage(bucket: String, key: String, content: ByteArray) {
//        s3Client.putObject(
//            { it.bucket(bucket).key(key) },
//            RequestBody.fromBytes(content)
//        )
//    }
//
//    fun getPresignedURL(bucket: String, key: String): String{
//
//    }
//}
