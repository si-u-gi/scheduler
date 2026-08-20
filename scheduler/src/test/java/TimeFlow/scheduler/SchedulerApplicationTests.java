package TimeFlow.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import TimeFlow.scheduler.entity.User;
import TimeFlow.scheduler.repository.UserRepository;
import jakarta.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class SchedulerApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void contextLoads() {
	}

	@Test
	void 로그인_컨트롤러_테스트() throws Exception {
		mockMvc.perform(get("/login"))
				.andExpect(status().isOk())
				.andExpect(view().name("login"));
	}

	@Test
	void 회원가입_컨트롤러_테스트() throws Exception {
		mockMvc.perform(get("/signup"))
				.andExpect(status().isOk())
				.andExpect(view().name("signup"));
	}

	@Test
	void 실제_로그인_테스트() throws Exception {

		User user = new User();
		user.setUsername("siugi");
		user.setPassword(passwordEncoder.encode("1234"));

		userRepository.save(user);

		mockMvc.perform(post("/login")
				.with(csrf())
				.param("username", "siugi")
				.param("password", "1234"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/dashboard"));
	}
}
