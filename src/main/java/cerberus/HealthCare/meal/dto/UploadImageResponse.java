package cerberus.HealthCare.meal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import cerberus.HealthCare.meal.entity.MealItem;
import cerberus.HealthCare.logmeal.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class UploadImageResponse {
    private String foodName;
    private NutritionInfo nutritionInfo;
}
