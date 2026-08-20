package TimeFlow.scheduler.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import TimeFlow.scheduler.dto.SignupRequest;
import TimeFlow.scheduler.entity.User;
import TimeFlow.scheduler.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }

    public void signup(SignupRequest request) {
        if (isUsernameTaken(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (isEmailTaken(request.getEmail())) {
            throw new IllegalArgumentException("Email is already taken");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setGender(request.getGender());
        user.setBirthDate(request.getBirthDate());
        userRepository.save(user);
    }
}
