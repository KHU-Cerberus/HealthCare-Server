package cerberus.HealthCare.user.repository;

import cerberus.HealthCare.user.entity.HealthReport;
import cerberus.HealthCare.user.entity.User;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthReportRepository extends JpaRepository<HealthReport, Long> {

    Optional<HealthReport> findByUserAndDate(User user, LocalDate date);
}
