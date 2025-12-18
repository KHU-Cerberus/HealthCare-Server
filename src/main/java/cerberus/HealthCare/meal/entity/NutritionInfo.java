package cerberus.HealthCare.meal.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionInfo {

    private Double calories;
    private Double totalFat;
    private Double saturatedFat;
    private Double cholesterol;
    private Double sodium;
    private Double totalCarbs;
    private Double fiber;
    private Double sugar;
    private Double protein;
}