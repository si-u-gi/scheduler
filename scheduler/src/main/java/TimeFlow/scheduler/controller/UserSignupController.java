package TimeFlow.scheduler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import TimeFlow.scheduler.dto.SignupRequest;
import TimeFlow.scheduler.service.UserService;

@Controller
public class UserSignupController {
    private final UserService userService;

    public UserSignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(SignupRequest signupRequest) {
        try {
            userService.signup(signupRequest);
        } catch (IllegalArgumentException e) {
            return "redirect:/signup?error=" + e.getMessage();
        }

        return "redirect:/login";
    }
}