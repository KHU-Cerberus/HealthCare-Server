package cerberus.HealthCare.meal.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UploadImageRequest {
    MultipartFile file;
}
