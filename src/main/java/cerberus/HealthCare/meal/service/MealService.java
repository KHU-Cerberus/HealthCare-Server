package cerberus.HealthCare.meal.service;

import cerberus.HealthCare.global.exception.CoreException;
import cerberus.HealthCare.global.exception.code.UserErrorCode;
import cerberus.HealthCare.meal.entity.Meal;
import cerberus.HealthCare.meal.entity.MealItem;
import cerberus.HealthCare.meal.repository.MealRepository;
import cerberus.HealthCare.user.entity.User;
import cerberus.HealthCare.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import cerberus.HealthCare.logmeal.LogMealService;
import cerberus.HealthCare.logmeal.FoodRecognitionResult;
import cerberus.HealthCare.logmeal.NutritionInfo;
import cerberus.HealthCare.meal.dto.SaveMealRequest;
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
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MealService {

    private final LogMealService logMealService;
    private final MealRepository mealRepository;
    private final UserRepository userRepository;

    public long getTodayMealCount(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = start.plusDays(1);
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

    @Transactional
    public Meal createMeal(SaveMealRequest request, String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new CoreException(UserErrorCode.USER_NOT_FOUND));

        // Meal 생성
        Meal meal = Meal.builder()
                .user(user)
                .eatTime(request.getEatTime() != null ? request.getEatTime() : LocalDateTime.now())
                .build();

        // MealItem 생성 및 추가
        NutritionInfo info = request.getNutritionInfo();
        MealItem mealItem = MealItem.builder()
                .foodName(request.getFoodName())
                .calories(info.getCalories())
                .totalFat(info.getTotalFat())
                .saturatedFat(info.getSaturatedFat())
                .cholesterol(info.getCholesterol())
                .sodium(info.getSodium())
                .totalCarbs(info.getTotalCarbs())
                .fiber(info.getFiber())
                .sugar(info.getSugar())
                .protein(info.getProtein())
                .createdAt(LocalDateTime.now())
                .build();

        meal.addMealItem(mealItem);

        return mealRepository.save(meal);
    }

    @Transactional
    public Meal createMealFromUpload(UploadImageResponse uploadResponse, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        // Meal 생성
        Meal meal = Meal.builder()
                .user(user)
                .eatTime(LocalDateTime.now())
                .build();

        // MealItem 생성 및 추가
        NutritionInfo info = uploadResponse.getNutritionInfo();
        MealItem mealItem = MealItem.builder()
                .foodName(uploadResponse.getFoodName())
                .calories(info.getCalories())
                .totalFat(info.getTotalFat())
                .saturatedFat(info.getSaturatedFat())
                .cholesterol(info.getCholesterol())
                .sodium(info.getSodium())
                .totalCarbs(info.getTotalCarbs())
                .fiber(info.getFiber())
                .sugar(info.getSugar())
                .protein(info.getProtein())
                .createdAt(LocalDateTime.now())
                .build();

        meal.addMealItem(mealItem);

        return mealRepository.save(meal);
    }
}