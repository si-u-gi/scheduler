package TimeFlow.scheduler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import TimeFlow.scheduler.service.UserService;

@Controller
public class UserLoginController {
    private final UserService userService;

    public UserLoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // @PostMapping("/login")
    // public String login(String username, String password) {
    // userService.login(username, password);
    // return "redirect:/dashboard";
    // }
}
