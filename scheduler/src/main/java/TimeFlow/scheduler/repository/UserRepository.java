package TimeFlow.scheduler.repository;

import org.springframework.stereotype.Repository;
import TimeFlow.scheduler.entity.User;

@Repository
public interface UserRepository {
    User save(User user);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
