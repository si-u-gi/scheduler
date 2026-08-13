package TimeFlow.scheduler.service;

import org.springframework.stereotype.Service;

import TimeFlow.scheduler.dto.SignupRequest;
import TimeFlow.scheduler.entity.User;
import TimeFlow.scheduler.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        user.setPassword(request.getPassword());
        user.setGender(request.getGender());
        user.setBirthDate(request.getBirthDate());
        userRepository.save(user);
    }
}
