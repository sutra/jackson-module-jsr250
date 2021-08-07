package org.oxerr.jackson.module.jsr250;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.Serializable;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class Jsr250ModuleTest {

	private User user;

	@BeforeEach
	void setUp() throws Exception {
		this.user = new User();
		user.setNickname("John Doe");
		user.setUsername("john");
		user.setPassword("secret");
	}

	@Test
	void testNoJsr250Module() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();

		assertEquals("{\"nickname\":\"John Doe\",\"username\":\"john\",\"password\":\"secret\"}", objectMapper.writeValueAsString(user));
	}

	@Test
	void testJsr250Module() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new Jsr250Module());

		assertEquals("{\"nickname\":\"John Doe\"}", objectMapper.writeValueAsString(user));

		Authentication userAuthentication = new TestingAuthenticationToken("user", "pass", "ROLE_USER");
		SecurityContextHolder.getContext().setAuthentication(userAuthentication);
		assertEquals("{\"nickname\":\"John Doe\"}", objectMapper.writeValueAsString(user));

		Authentication adminAuthentication = new TestingAuthenticationToken("user", "pass", "ROLE_USER", "ROLE_ADMIN");
		SecurityContextHolder.getContext().setAuthentication(adminAuthentication);
		assertEquals("{\"nickname\":\"John Doe\",\"username\":\"john\"}", objectMapper.writeValueAsString(user));
	}

	public static class User implements Serializable {

		private static final long serialVersionUID = 2021080801L;

		private String nickname;

		private String username;

		private String password;

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		@RolesAllowed("ROLE_ADMIN")
		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		@DenyAll
		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

	}

}
