//package cerberus.HealthCare.aws
//
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import software.amazon.awssdk.services.s3.S3Client
//import software.amazon.awssdk.services.s3.S3Configuration
//
//@Configuration
//class S3ClientProvider(
//    private val region: software.amazon.awssdk.regions.Region,
//    private val credentialsProvider: software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
//) {
//
//    @Bean
//    fun s3Client(): S3Client {
//        return S3Client.builder()
//            .region(region)
//            .credentialsProvider(credentialsProvider)
//            .serviceConfiguration(
//                S3Configuration.builder().checksumValidationEnabled(false).build()
//            )
//            .build()
//    }
//}