package cerberus.HealthCare.user.repository;

import cerberus.HealthCare.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String username);
}
