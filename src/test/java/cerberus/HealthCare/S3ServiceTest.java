package cerberus.HealthCare;

import aws.smithy.kotlin.runtime.content.ByteStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.nio.charset.StandardCharsets;

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
        String key = "test/test-upload.txt";
        String content = "Hello from Java Test!";

        ByteStream bytes = ByteStream.Companion.fromBytes(
                content.getBytes(StandardCharsets.UTF_8)
        );

        s3Service.uploadImageBlocking(key, bytes, "text/plain");
        System.out.println("Upload success!");
    }

    @Test
    void testGetPresignedUrl() throws Exception {
        String key = "test/test-upload.txt";
        String url = s3Service.getPresignedUrlBlocking(key, 600);

        System.out.println("Download Presigned URL: " + url);
    }

    @Test
    void testDeleteImage() throws Exception {
        String key = "test/test-upload.txt";
        s3Service.deleteImageBlocking(key);
        System.out.println("delete success!");
    }
}