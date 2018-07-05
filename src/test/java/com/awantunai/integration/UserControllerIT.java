package com.awantunai.integration;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.awantunai.AtbankApplication;
import com.awantunai.entities.User;
import com.awantunai.repositories.UserRepository;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AtbankApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
public class UserControllerIT {
	@Autowired
	private UserRepository userRepository;
	@LocalServerPort
	private int port;
	private TestRestTemplate restTemplate = new TestRestTemplate();
	private HttpHeaders headers = new HttpHeaders();
	private User user;

	public void init(String username) {
		user = User.builder().username(username).password("def").build();
		userRepository.saveAndFlush(user);
	}

	@Test
	public void test_create_user_success() {
		Map payload = new HashMap();
		payload.put("username", "asd");
		payload.put("password", "pass2");

		HttpEntity<?> entity = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/api/users"),
			HttpMethod.POST, entity, String.class);

		Assert.assertNotNull(response.getBody());
	}

	@Test
	public void test_create_user_failure() throws Exception{
		init("qwe");
		Map payload = new HashMap();
		payload.put("username", "qwe");
		payload.put("password", "pass1");

		HttpEntity<?> entity = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/api/users"),
			HttpMethod.POST, entity, String.class);

		Assert.assertNotNull(response.getBody());
		JSONAssert.assertEquals("{\"message\":\"The username qwe is already exits, Please select a different username.\",\"code\":\"PRECONDITION_FAILED\"}", response.getBody(), false);
	}

	@Test
	public void test_update_user_success() {
		init("abc");
		Map payload = new HashMap();
		payload.put("username", "abc");
		payload.put("password", "pass");

		HttpEntity<?> entity = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/api/users"),
			HttpMethod.PUT, entity, String.class);

		Assert.assertNotNull(response.getBody());
	}

	@Test
	public void test_update_user_failure() throws Exception{
		Map payload = new HashMap();
		payload.put("username", "abc1");
		payload.put("password", "pass");

		HttpEntity<?> entity = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/api/users"),
			HttpMethod.PUT, entity, String.class);

		Assert.assertNotNull(response.getBody());
		JSONAssert.assertEquals("{\"message\":\"The username abc1 does not exists.\",\"code\":\"PRECONDITION_FAILED\"}", response.getBody(), false);
	}

	@Test
	public void test_delete_user_success() {
		Map payload = new HashMap();
		payload.put("username", "abc");

		HttpEntity<?> entity = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/api/users"),
			HttpMethod.DELETE, entity, String.class);

		Assert.assertNotNull(response.getBody());
	}

	@After
	public void tearDown() {
		Map payload = new HashMap();
		payload.put("username", "asd");
		HttpEntity<?> entity = new HttpEntity<>(payload, headers);
		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/api/users"),
			HttpMethod.DELETE, entity, String.class);


		payload = new HashMap();
		payload.put("username", "qwe");
		entity = new HttpEntity<>(payload, headers);
		response = restTemplate.exchange(
			createURLWithPort("/api/users"),
			HttpMethod.DELETE, entity, String.class);

		payload = new HashMap();
		payload.put("username", "abc1");
		entity = new HttpEntity<>(payload, headers);
		response = restTemplate.exchange(
			createURLWithPort("/api/users"),
			HttpMethod.DELETE, entity, String.class);

		payload = new HashMap();
		payload.put("username", "abc");
		entity = new HttpEntity<>(payload, headers);
		response = restTemplate.exchange(
			createURLWithPort("/api/users"),
			HttpMethod.DELETE, entity, String.class);
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}
