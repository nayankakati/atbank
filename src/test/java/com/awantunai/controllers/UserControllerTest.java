package com.awantunai.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.awantunai.AtbankApplication;
import com.awantunai.entities.User;
import com.awantunai.exceptions.UserException;
import com.awantunai.helpers.ResponseBuilder;
import com.awantunai.requests.UserRequest;
import com.awantunai.services.AccountService;
import com.awantunai.services.TransactionService;
import com.awantunai.services.UserService;


@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class, secure = false)
@ContextConfiguration(classes = AtbankApplication.class)
public class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean(name = "userService")
	private UserService userService;
	@MockBean(name = "accountService")
	private AccountService accountService;
	@MockBean(name = "transactionService")
	private TransactionService transactionService;

	private User user;

	@Before
	public void init() {
		user = getUser();
	}

	@Test
	public void test_create_user_success() throws Exception {
		UserRequest userRequest = UserRequest.builder().username("abc").password("def").build();

		when(userService.createUser(any())).thenReturn(user);

		RequestBuilder builder = MockMvcRequestBuilders.post("/api/users/").contentType(MediaType.APPLICATION_JSON).content(ResponseBuilder.asJsonString(userRequest)).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals("{\"id\":2,\"username\":\"abc\"}", mvcResult.getResponse().getContentAsString(), false);
	}

	@Test
	public void test_create_user_failure() throws Exception {
		UserRequest userRequest = UserRequest.builder().username("abc").password("def").build();

		when(userService.createUser(any())).thenThrow(new UserException("Exception"));

		RequestBuilder builder = MockMvcRequestBuilders.post("/api/users/").contentType(MediaType.APPLICATION_JSON).content(ResponseBuilder.asJsonString(userRequest)).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals("{\"message\":\"Exception\",\"code\":\"PRECONDITION_FAILED\"}", mvcResult.getResponse().getContentAsString(), false);
	}

	@Test
	public void test_update_user_success() throws Exception {
		UserRequest userRequest = UserRequest.builder().username("abc").password("def").build();

		when(userService.updateUser(any())).thenReturn(user);

		RequestBuilder builder = MockMvcRequestBuilders.put("/api/users/").contentType(MediaType.APPLICATION_JSON).content(ResponseBuilder.asJsonString(userRequest)).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals("{\"id\":2,\"username\":\"abc\"}", mvcResult.getResponse().getContentAsString(), false);
	}

	@Test
	public void test_update_user_failure() throws Exception {
		UserRequest userRequest = UserRequest.builder().username("abc").password("def").build();

		when(userService.updateUser(any())).thenThrow(new UserException("Exception"));

		RequestBuilder builder = MockMvcRequestBuilders.put("/api/users/").contentType(MediaType.APPLICATION_JSON).content(ResponseBuilder.asJsonString(userRequest)).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals("{\"message\":\"Exception\",\"code\":\"PRECONDITION_FAILED\"}", mvcResult.getResponse().getContentAsString(), false);
	}

	@Test
	public void test_update_user_failure_no_password() throws Exception {
		UserRequest userRequest = UserRequest.builder().username("abc").build();

		when(userService.updateUser(any())).thenThrow(new UserException("Exception"));

		RequestBuilder builder = MockMvcRequestBuilders.put("/api/users/").contentType(MediaType.APPLICATION_JSON).content(ResponseBuilder.asJsonString(userRequest)).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals("{\"message\":\"Password is mandatory.\",\"code\":\"PRECONDITION_FAILED\"}", mvcResult.getResponse().getContentAsString(), false);
	}

	@Test
	public void test_delete_user_success() throws Exception {
		UserRequest userRequest = UserRequest.builder().username("abc").password("def").build();

		when(userService.deleteUser(any())).thenReturn(user);

		RequestBuilder builder = MockMvcRequestBuilders.delete("/api/users/").contentType(MediaType.APPLICATION_JSON).content(ResponseBuilder.asJsonString(userRequest)).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals("{\"id\":2,\"username\":\"abc\"}", mvcResult.getResponse().getContentAsString(), false);
	}

	@Test
	public void test_delete_user_failure() throws Exception {
		UserRequest userRequest = UserRequest.builder().username("abc").password("def").build();

		when(userService.deleteUser(any())).thenThrow(new UserException("Exception"));

		RequestBuilder builder = MockMvcRequestBuilders.delete("/api/users/").contentType(MediaType.APPLICATION_JSON).content(ResponseBuilder.asJsonString(userRequest)).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals("{\"message\":\"Exception\",\"code\":\"PRECONDITION_FAILED\"}", mvcResult.getResponse().getContentAsString(), false);
	}

	private User getUser() {
		return User.builder().id(2l).username("abc").password("qwerty").build();
	}

}
