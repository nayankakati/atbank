package com.awantunai.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.awantunai.exceptions.AccountException;
import com.awantunai.helpers.ResponseBuilder;
import com.awantunai.requests.AccountRequest;
import com.awantunai.requests.AccountUpdateRequest;
import com.awantunai.services.AccountService;
import com.awantunai.services.UserService;


@RunWith(SpringRunner.class)
@WebMvcTest(value = AccountController.class, secure = false)
@ContextConfiguration(classes = AtbankApplication.class)
public class AccountControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean(name = "accountService")
	private AccountService accountService;
	@MockBean(name = "userService")
	private UserService userService;

	private Account account;
	private User user;
	private AccountRequest accountRequest;
	private AccountUpdateRequest accountUpdateRequest;

	@Before
	public void init() {
		user = getUser();
		account = getAccount();

	}

	@Test
	public void test_accounts_creation_success() throws Exception {
		AccountRequest accountRequest = AccountRequest.builder().amount(new BigDecimal(100)).userId(1l).build();
		when(userService.findUser(anyLong())).thenReturn(user);
		when(accountService.createAccount(any(), any())).thenReturn(account);
		RequestBuilder builder = MockMvcRequestBuilders.post("/api/accounts/").contentType(MediaType.APPLICATION_JSON).content(ResponseBuilder.asJsonString(accountRequest)).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals("{\"id\":1,\"user\":{\"id\":2,\"username\":\"abc\"},\"accountType\":\"SAVINGS\",\"accountStatus\":\"ACTIVE\",\"createdOn\":\"2018-06-30T20:39:03.000+0000\",\"balance\":100}", mvcResult.getResponse().getContentAsString(), false);
	}

	@Test
	public void test_accounts_creation_failure() throws Exception {
		AccountRequest accountRequest = AccountRequest.builder().amount(new BigDecimal(100)).userId(1l).build();
		when(userService.findUser(anyLong())).thenThrow(new AccountException("Exception"));
		when(accountService.createAccount(any(), any())).thenReturn(account);
		RequestBuilder builder = MockMvcRequestBuilders.post("/api/accounts/").contentType(MediaType.APPLICATION_JSON).content(ResponseBuilder.asJsonString(accountRequest)).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		System.out.println(mvcResult.getResponse().getContentAsString());
		JSONAssert.assertEquals("{\"message\":\"Exception\",\"code\":\"PRECONDITION_FAILED\"}", mvcResult.getResponse().getContentAsString(), false);
	}

	@Test
	public void test_accounts_updation_success() throws Exception {
		AccountUpdateRequest accountRequest = AccountUpdateRequest.builder().accountId(1l).accountStatus(AccountStatus.CLOSED).build();
		when(accountService.updateAccount(any())).thenReturn(account);
		RequestBuilder builder = MockMvcRequestBuilders.put("/api/accounts/").contentType(MediaType.APPLICATION_JSON).content(ResponseBuilder.asJsonString(accountRequest)).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals("{\"id\":1,\"user\":{\"id\":2,\"username\":\"abc\"},\"accountType\":\"SAVINGS\",\"accountStatus\":\"ACTIVE\",\"createdOn\":\"2018-06-30T20:39:03.000+0000\",\"balance\":100}", mvcResult.getResponse().getContentAsString(), false);
	}

	@Test
	public void test_accounts_updation_failure() throws Exception {
		AccountUpdateRequest accountRequest = AccountUpdateRequest.builder().accountId(1l).accountStatus(AccountStatus.CLOSED).build();
		when(accountService.updateAccount(any())).thenThrow(new AccountException("Exception"));
		RequestBuilder builder = MockMvcRequestBuilders.put("/api/accounts/").contentType(MediaType.APPLICATION_JSON).content(ResponseBuilder.asJsonString(accountRequest)).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals("{\"message\":\"Exception\",\"code\":\"PRECONDITION_FAILED\"}", mvcResult.getResponse().getContentAsString(), false);
	}

	@Test
	public void test_accounts_get_success() throws Exception {
		when(accountService.getAccount(any())).thenReturn(account);
		RequestBuilder builder = MockMvcRequestBuilders.get("/api/accounts/1").accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals("[{\"id\":1,\"user\":{\"id\":2,\"username\":\"abc\"},\"accountType\":\"SAVINGS\",\"accountStatus\":\"ACTIVE\",\"createdOn\":\"2018-06-30T20:39:03.000+0000\",\"balance\":100}]", mvcResult.getResponse().getContentAsString(), false);
	}

	@Test
	public void test_accounts_get_failure() throws Exception {
		when(accountService.getAccount(any())).thenThrow(new AccountException("Exception"));
		RequestBuilder builder = MockMvcRequestBuilders.get("/api/accounts/1").accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals("[{\"message\":\"Exception\",\"code\":\"PRECONDITION_FAILED\"}]", mvcResult.getResponse().getContentAsString(), false);
	}

	private Account getAccount() {
		return Account.builder().id(1l).userId(getUser()).balance(new BigDecimal(100)).accountStatus(AccountStatus.ACTIVE).accountType(AccountType.SAVINGS).createdOn(new Date(1530391143000l)).build();
	}
	private User getUser() {
		return User.builder().id(2l).username("abc").password("qwerty").build();
	}

}
