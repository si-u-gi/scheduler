package TimeFlow.scheduler.repository;

import TimeFlow.scheduler.entity.User;
import jakarta.persistence.EntityManager;

public class JpaUserRepository implements UserRepository {
    private final EntityManager em;

    public JpaUserRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public boolean existsByUsername(String username) {
        String query = "SELECT COUNT(u) FROM User u WHERE u.username = :username";
        Long count = em.createQuery(query, Long.class)
                .setParameter("username", username)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        String query = "SELECT COUNT(u) FROM User u WHERE u.email = :email";
        Long count = em.createQuery(query, Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public User save(User user) {
        em.persist(user);
        return user;
    }
}
