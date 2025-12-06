package cerberus.HealthCare.meal.service;

import cerberus.HealthCare.meal.repository.MealRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final MealRepository mealRepository;

    public long getTodayMealCount(Long userId) {

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();        // 00:00
        LocalDateTime end = start.plusDays(1);             // 내일 00:00

        return mealRepository.countByUserIdAndEatTimeBetween(userId, start, end);
    }

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