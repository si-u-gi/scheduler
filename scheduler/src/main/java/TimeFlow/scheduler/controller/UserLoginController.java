package TimeFlow.scheduler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserLoginController {
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(String username, String password) {
        // Implement your login logic here
        // For example, you can check the username and password against the database
        // If the login is successful, return a success message or redirect to another
        // page
        // If the login fails, return an error message or redirect back to the login
        // page
        return "Login successful"; // Placeholder response
    }
}
