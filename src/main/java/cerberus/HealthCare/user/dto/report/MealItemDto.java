package cerberus.HealthCare.user.dto.report;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MealItemDto {
    private String foodName;
    private Double calories; //칼로리
    private Double totalFat;         // 총 지방
    private Double saturatedFat; // 포화지방
    private Double cholesterol; // 콜레스테롤
    private Double sodium; // 나트륨
    private Double totalCarbs;//탄수화물
    private Double fiber;    //식이섬유
    private Double sugar; // 당류
    private Double protein;     //단백질
}

