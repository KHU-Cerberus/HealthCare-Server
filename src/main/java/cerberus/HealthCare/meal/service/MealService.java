package cerberus.HealthCare.meal.service;

import cerberus.HealthCare.logmeal.LogMealService;
import cerberus.HealthCare.logmeal.FoodRecognitionResult;
import cerberus.HealthCare.logmeal.NutritionInfo;
import cerberus.HealthCare.meal.dto.UploadImageRequest;
import cerberus.HealthCare.meal.dto.UploadImageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.InputStream;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor // final field injection
@Transactional(readOnly = true)
public class MealService {
    private final LogMealService logMealService;

    @Transactional
    public UploadImageResponse upload(UploadImageRequest uploadImageRequest) {
        try {
            InputStream fileInputStream = uploadImageRequest.getFile().getInputStream();
            FoodRecognitionResult result = logMealService.imageRecognition(fileInputStream);
            NutritionInfo info = logMealService.getNutrition(result.getFoodId(), result.getImageId());
            return new UploadImageResponse(result.getFoodName(), info);
        } catch (IOException e) {
            log.error("파일 데이터를 읽는 중 I/O 오류 발생", e);
            throw new RuntimeException("파일 데이터를 읽는 중 오류가 발생했습니다.", e);
        }
    }
}