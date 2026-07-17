package com.oyku.event_reservation_api.integration;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.oyku.event_reservation_api.dto.event.EventCreateRequest;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

	@Test
	@WithMockUser(username = "email222@gmail.com", roles = "USER")
	void shouldReturnForbiddenWhenUserCreatesEvent() throws Exception {

		EventCreateRequest request = createRequest();

		mockMvc.perform(
				post("/api/events").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
				.andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())

				.andExpect(status().isForbidden())
				.andDo(print());
	}

	@Test
	@WithMockUser(username = "email1@gmail.com", roles = "ADMIN")
	void shouldAllowAdminToCreateEvent() throws Exception {

		EventCreateRequest request = createRequest();

		mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated());
	}

	private EventCreateRequest createRequest() {

		EventCreateRequest request = new EventCreateRequest();
		request.setName("Concert " + System.currentTimeMillis());
		request.setAddress("Ankara");
		request.setLocation("Arena");
		request.setSeatCount(50);
		request.setTicketPrice(BigDecimal.valueOf(500));
		request.setEventDate(LocalDateTime.now().plusDays(5));

		return request;
	}
}