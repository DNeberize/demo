package com.github.DNeberize.demo;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import com.github.DNeberize.demo.repository.AnswerWordRepository;
import com.github.DNeberize.demo.repository.WordEntryRepository;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:worde-test;DB_CLOSE_DELAY=-1",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WordEntryRepository wordEntryRepository;

	@Autowired
	private AnswerWordRepository answerWordRepository;

	@Test
	void contextLoads() {
		assertThat(wordEntryRepository.count()).isGreaterThan(1000);
		assertThat(answerWordRepository.count()).isGreaterThan(0);
	}

	@Test
	void homePageRenders() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Worde")));
	}

	@Test
	void guessEndpointReturnsOnlyFiveScoreValues() throws Exception {
		MockHttpSession session = new MockHttpSession();
		String guess = wordEntryRepository.findAll(PageRequest.of(0, 1))
				.stream()
				.findFirst()
				.orElseThrow()
				.getValue();

		mockMvc.perform(post("/session/user")
				.session(session)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("username", "tester01"))
				.andExpect(status().is3xxRedirection());

		mockMvc.perform(post("/api/game/guess")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"guess\":\"" + guess + "\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.guess").value(guess))
				.andExpect(jsonPath("$.result", hasSize(5)))
				.andExpect(jsonPath("$.revealedWord").doesNotExist())
				.andExpect(content().string(not(containsString("secretWord"))));
	}

	@Test
	void guessEndpointRejectsSymbolsAndNumbers() throws Exception {
		MockHttpSession session = new MockHttpSession();

		mockMvc.perform(post("/session/user")
				.session(session)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("username", "tester02"))
				.andExpect(status().is3xxRedirection());

		mockMvc.perform(post("/api/game/guess")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"guess\":\"ab12!\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Enter a valid 5-letter word with letters only."));
	}

}
