package zovryn.finance_dashboard.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import zovryn.finance_dashboard.model.UserData;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserData, Integer> {
    Optional<UserData> findByUsername(String username);
    Optional<UserData> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
