package cerberus.HealthCare.aws;

import aws.smithy.kotlin.runtime.content.ByteStream;
import cerberus.HealthCare.aws.service.S3Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

@SpringJUnitConfig
public class S3ServiceTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public S3Service s3Service() {
            return new S3Service();
        }
    }

    @Autowired
    private S3Service s3Service;

    @Test
    void testUploadImage() throws Exception {
        String key = "test/pizza.jpg";

        // resources/pizza.jpg 파일 읽기
        ClassPathResource resource = new ClassPathResource("pizza.jpg");
        InputStream inputStream = resource.getInputStream();
        byte[] imageBytes = inputStream.readAllBytes();
        inputStream.close();

        ByteStream bytes = ByteStream.Companion.fromBytes(imageBytes);

        s3Service.uploadImageBlocking(key, bytes, "image/jpeg");
        System.out.println("Upload success!");
    }


    @Test
    void testGetPresignedUrl() throws Exception {
        String key = "test/pizza.jpg";
        String url = s3Service.getPresignedUrlBlocking(key, 600);

        System.out.println("Download Presigned URL: " + url);
    }

    /*
    @Test
    void testDeleteImage() throws Exception {
        String key = "test/test-upload.txt";
        s3Service.deleteImageBlocking(key);
        System.out.println("delete success!");
    }
    */
}