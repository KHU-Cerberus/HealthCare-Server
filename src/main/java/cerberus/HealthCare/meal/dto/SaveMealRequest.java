package cerberus.HealthCare.meal.dto;

import cerberus.HealthCare.logmeal.NutritionInfo;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveMealRequest {
    private LocalDateTime eatTime;
    private String foodName;
    private NutritionInfo nutritionInfo;
}