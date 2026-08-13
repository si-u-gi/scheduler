package TimeFlow.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import TimeFlow.scheduler.repository.UserRepository;
import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class SchedulerApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

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

	void 실제_회원가입_테스트() throws Exception {
		mockMvc.perform(post("/signup")
				.param("name", "신현욱")
				.param("username", "siugi")
				.param("email", "siugi@test.com")
				.param("phone", "010-1234-5678")
				.param("password", "1234")
				.param("ConfirmPassword", "1234")
				.param("gender", "남")
				.param("birthDate", "2004-01-01"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login"));
				
		boolean exists = userRepository.existsByEmail("siugi@test.com");
		assertTrue(exists);
	}
}
