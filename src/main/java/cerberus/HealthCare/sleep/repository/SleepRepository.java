package cerberus.HealthCare.sleep.repository;

import cerberus.HealthCare.sleep.entity.SleepLog;
import cerberus.HealthCare.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SleepRepository extends JpaRepository<SleepLog, Long> {

    List<SleepLog> findAllByUserAndStartAfterOrderByStartDesc(User user, LocalDateTime after);

    Optional<SleepLog> findByIdAndUser(Long id, User user);

    List<SleepLog> findByUserIdAndStartBetween(Long userId, LocalDateTime start, LocalDateTime end);

}
