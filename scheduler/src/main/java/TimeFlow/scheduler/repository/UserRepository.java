package TimeFlow.scheduler.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import TimeFlow.scheduler.entity.User;

@Repository
public interface UserRepository {
    User save(User user);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
