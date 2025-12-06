package cerberus.HealthCare.user.dto.report;

import cerberus.HealthCare.meal.entity.Meal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MealDto {
    private LocalDateTime eatTime;
    private String foodImage;
    private List<MealItemDto> items;

    public static MealDto toMealDto(Meal meal) {
        List<MealItemDto> items = meal.getMealItems().stream()
            .map(i -> new MealItemDto(
                i.getFoodName(),     // 음식명
                i.getCalories(),     // 칼로리
                i.getTotalFat(),    // 총 지방
                i.getSaturatedFat(),    // 포화지방
                i.getCholesterol(), //콜레스테롤
                i.getSodium(),  // 나트륨
                i.getTotalCarbs(),   // 탄수화물
                i.getFiber(),         // 식이섬유
                i.getSugar(),   // 당류
                i.getProtein()      // 단백질
            ))
            .toList();

        return new MealDto(
            meal.getEatTime(),
            meal.getFoodImage(),
            items
        );
    }
}
