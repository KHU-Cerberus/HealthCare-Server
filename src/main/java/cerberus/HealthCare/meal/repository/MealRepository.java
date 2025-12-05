package cerberus.HealthCare.meal.repository;

import cerberus.HealthCare.meal.entity.Meal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByUserIdAndEatTimeBetween(Long userId, LocalDateTime start, LocalDateTime end);


}
