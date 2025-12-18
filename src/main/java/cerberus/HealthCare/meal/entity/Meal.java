package cerberus.HealthCare.meal.entity;

import cerberus.HealthCare.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "eat_time")
    private LocalDateTime eatTime;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MealItem> mealItems = new ArrayList<>();

    // MealItem 추가 헬퍼 메서드
    public void addMealItem(MealItem mealItem) {
        mealItems.add(mealItem);
        mealItem.setMeal(this);
    }
}