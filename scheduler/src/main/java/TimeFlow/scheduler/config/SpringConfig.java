package TimeFlow.scheduler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import TimeFlow.scheduler.repository.JpaUserRepository;
import TimeFlow.scheduler.repository.UserRepository;
import jakarta.persistence.EntityManager;

@Configuration
public class SpringConfig {
    private final EntityManager em;

    public SpringConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public UserRepository userRepository() {
        return new JpaUserRepository(em);
    }
}
