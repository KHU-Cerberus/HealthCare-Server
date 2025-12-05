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
                i.getFoodName(),
                i.getMass(),
                i.getCalories(),
                i.getCarbohydrate(),
                i.getProtein(),
                i.getFat(),
                i.getRoughage()
            ))
            .toList();

        return new MealDto(
            meal.getEatTime(),
            meal.getFoodImage(),
            items
        );
    }
}
