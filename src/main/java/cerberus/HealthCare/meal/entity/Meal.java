package cerberus.HealthCare.meal.entity;

import cerberus.HealthCare.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    @Column(name = "food_image", length = 500)
    private String foodImage;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL)
    private List<MealItem> mealItems;
}

