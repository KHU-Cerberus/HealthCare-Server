package cerberus.HealthCare.meal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "meal_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "food_name")
    private String foodName;

    @ManyToOne
    @JoinColumn(name = "meal_id")
    private Meal meal;

    private String mass;

    private BigDecimal calories;    //칼로리
    private BigDecimal carbohydrate;//탄수화물
    private BigDecimal protein;     //단백질
    private BigDecimal fat;         //지방
    private BigDecimal roughage;    //식이섬유

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
