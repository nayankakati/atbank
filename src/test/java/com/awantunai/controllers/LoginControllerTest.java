package com.awantunai.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;

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
import com.awantunai.entities.Account;
import com.awantunai.entities.User;
import com.awantunai.enums.AccountStatus;
import com.awantunai.enums.AccountType;
import com.awantunai.exceptions.UserException;
import com.awantunai.helpers.ResponseBuilder;
import com.awantunai.requests.UserRequest;
import com.awantunai.services.AccountService;
import com.awantunai.services.TransactionService;
import com.awantunai.services.UserService;


@RunWith(SpringRunner.class)
@WebMvcTest(value = LoginController.class, secure = false)
@ContextConfiguration(classes = AtbankApplication.class)

public class LoginControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean(name = "userService")
	private UserService userService;
	@MockBean(name = "accountService")
	private AccountService accountService;
	@MockBean(name = "transactionService")
	private TransactionService transactionService;
	private User user;
	private Account account;

	@Before
	public void init() {
		user = getUser();
		account = Account.builder().id(1l).userId(getUser()).balance(new BigDecimal(100)).accountStatus(AccountStatus.ACTIVE).accountType(AccountType.SAVINGS).createdOn(new Date(1530391143000l)).build();
	}

	@Test
	public void test_login_success() throws Exception {
		UserRequest userRequest = UserRequest.builder().username("abc").password("qwerty").build();
		when(userService.loginUser(any())).thenReturn(account);
		RequestBuilder builder = MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(ResponseBuilder.asJsonString(userRequest)).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals("{\"id\":1,\"user\":{\"id\":2,\"username\":\"abc\"},\"accountType\":\"SAVINGS\",\"accountStatus\":\"ACTIVE\",\"createdOn\":\"2018-06-30T20:39:03.000+0000\",\"balance\":100}", mvcResult.getResponse().getContentAsString(), false);
	}

	@Test
	public void test_login_failure() throws Exception {
		UserRequest userRequest = UserRequest.builder().username("abc").password("qwerty").build();
		when(userService.loginUser(any())).thenThrow(new UserException("Exception"));
		RequestBuilder builder = MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(ResponseBuilder.asJsonString(userRequest)).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals("{\"message\":\"Exception\",\"code\":\"PRECONDITION_FAILED\"}", mvcResult.getResponse().getContentAsString(), false);
	}

	private User getUser() {
		return User.builder().id(2l).username("abc").password("qwerty").build();
	}

}
