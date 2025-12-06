package cerberus.HealthCare.meal.entity;

import jakarta.persistence.*;
import lombok.*;

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

    private Double calories; //칼로리
    private Double totalFat;         // 총 지방
    private Double saturatedFat; // 포화지방
    private Double cholesterol; // 콜레스테롤
    private Double sodium; // 나트륨
    private Double totalCarbs;//탄수화물
    private Double fiber;    //식이섬유
    private Double sugar; // 당류
    private Double protein;     //단백질

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
