package com.awantunai.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static com.awantunai.helpers.ResponseBuilder.asJsonString;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.awantunai.AtbankApplication;
import com.awantunai.entities.Account;
import com.awantunai.entities.AccountTransactions;
import com.awantunai.entities.User;
import com.awantunai.enums.AccountStatus;
import com.awantunai.enums.AccountType;
import com.awantunai.enums.TransactionType;
import com.awantunai.exceptions.TransactionException;
import com.awantunai.requests.TransactionRequest;
import com.awantunai.services.AccountService;
import com.awantunai.services.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(SpringRunner.class)
@WebMvcTest(value = TransactionController.class, secure = false)
@ContextConfiguration(classes = AtbankApplication.class)
public class TransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean(name = "transactionService")
	private TransactionService transactionService;
	@MockBean(name = "accountService")
	private AccountService accountService;

	private Account account;
	private User user;

	@Before
	public void init() {
		user = getUser();
		account = getAccount();
	}

	@Test
	public void test_credit_transaction_success() throws Exception {
		TransactionRequest transactionRequest = TransactionRequest.builder().accountId(1l).amount(new BigDecimal(200)).build();
		when(accountService.findAccount(any())).thenReturn(account);
		when(transactionService.creditAmountToAccount(any(), any())).thenReturn(account);
		RequestBuilder builder = MockMvcRequestBuilders.post("/api/transactions/credit").contentType(MediaType.APPLICATION_JSON).content(asJsonString(transactionRequest)).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();

		JSONAssert.assertEquals("{\"id\":1,\"user\":{\"id\":2,\"username\":\"abc\"},\"accountType\":\"SAVINGS\",\"accountStatus\":\"ACTIVE\",\"createdOn\":\"2018-06-30T20:39:03.000+0000\",\"balance\":100}",mvcResult.getResponse().getContentAsString(),false);
	}

	@Test
	public void test_credit_transaction_failure() throws Exception {
		TransactionRequest transactionRequest = TransactionRequest.builder().accountId(1l).amount(new BigDecimal(200)).build();
		when(accountService.findAccount(any())).thenThrow(new TransactionException("Exception"));
		when(transactionService.creditAmountToAccount(any(), any())).thenReturn(account);
		RequestBuilder builder = MockMvcRequestBuilders.post("/api/transactions/credit").contentType(MediaType.APPLICATION_JSON).content(asJsonString(transactionRequest)).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();

		JSONAssert.assertEquals("{\"message\":\"Exception\",\"code\":\"PRECONDITION_FAILED\"}",mvcResult.getResponse().getContentAsString(),false);
	}

	@Test
	public void test_debit_transaction_success() throws Exception {
		TransactionRequest transactionRequest = TransactionRequest.builder().accountId(1l).amount(new BigDecimal(200)).build();
		when(accountService.findAccount(any())).thenReturn(account);
		when(transactionService.debitAmountToAccount(any(), any())).thenReturn(account);
		RequestBuilder builder = MockMvcRequestBuilders.post("/api/transactions/debit").contentType(MediaType.APPLICATION_JSON).content(asJsonString(transactionRequest)).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();

		JSONAssert.assertEquals("{\"id\":1,\"user\":{\"id\":2,\"username\":\"abc\"},\"accountType\":\"SAVINGS\",\"accountStatus\":\"ACTIVE\",\"createdOn\":\"2018-06-30T20:39:03.000+0000\",\"balance\":100}",mvcResult.getResponse().getContentAsString(),false);
	}

	@Test
	public void test_debit_transaction_failure() throws Exception {
		TransactionRequest transactionRequest = TransactionRequest.builder().accountId(1l).amount(new BigDecimal(200)).build();
		when(accountService.findAccount(any())).thenThrow(new TransactionException("Exception"));
		when(transactionService.debitAmountToAccount(any(), any())).thenReturn(account);
		RequestBuilder builder = MockMvcRequestBuilders.post("/api/transactions/debit").contentType(MediaType.APPLICATION_JSON).content(asJsonString(transactionRequest)).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();

		JSONAssert.assertEquals("{\"message\":\"Exception\",\"code\":\"PRECONDITION_FAILED\"}",mvcResult.getResponse().getContentAsString(),false);
	}

	@Test
	public void test_transfer_transaction_success() throws Exception {
		TransactionRequest transactionRequest = TransactionRequest.builder().accountId(1l).toAccountId(2l).amount(new BigDecimal(200)).build();
		when(accountService.findAccount(any())).thenReturn(account);
		when(transactionService.transferAmountToAccount(any(), any(), any())).thenReturn(account);
		RequestBuilder builder = MockMvcRequestBuilders.post("/api/transactions/transfer").contentType(MediaType.APPLICATION_JSON).content(asJsonString(transactionRequest)).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();

		JSONAssert.assertEquals("{\"id\":1,\"user\":{\"id\":2,\"username\":\"abc\"},\"accountType\":\"SAVINGS\",\"accountStatus\":\"ACTIVE\",\"createdOn\":\"2018-06-30T20:39:03.000+0000\",\"balance\":100}",mvcResult.getResponse().getContentAsString(),false);
	}

	@Test
	public void test_transfer_transaction_failure() throws Exception {
		TransactionRequest transactionRequest = TransactionRequest.builder().accountId(1l).toAccountId(2l).amount(new BigDecimal(200)).build();
		when(accountService.findAccount(any())).thenThrow(new TransactionException("Exception"));
		when(transactionService.transferAmountToAccount(any(), any(), any())).thenReturn(account);
		RequestBuilder builder = MockMvcRequestBuilders.post("/api/transactions/transfer").contentType(MediaType.APPLICATION_JSON).content(asJsonString(transactionRequest)).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();

		JSONAssert.assertEquals("{\"message\":\"Exception\",\"code\":\"PRECONDITION_FAILED\"}",mvcResult.getResponse().getContentAsString(),false);
	}

	@Test
	public void test_transfer_transaction_failue_without_to_account() throws Exception {
		TransactionRequest transactionRequest = TransactionRequest.builder().accountId(1l).amount(new BigDecimal(200)).build();
		when(accountService.findAccount(any())).thenThrow(new TransactionException("Exception"));
		when(transactionService.transferAmountToAccount(any(), any(), any())).thenReturn(account);
		RequestBuilder builder = MockMvcRequestBuilders.post("/api/transactions/transfer").contentType(MediaType.APPLICATION_JSON).content(asJsonString(transactionRequest)).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();

		JSONAssert.assertEquals("{\"message\":\"To account number is mandatory.\",\"code\":\"PRECONDITION_FAILED\"}",mvcResult.getResponse().getContentAsString(),false);
	}

	@Test
	public void test_list_transaction_success() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("from","1");
		params.add("to","2");
		params.add("size", "3");
		List<AccountTransactions> accountTransactionss = Arrays.asList(getAccountTransactions(TransactionType.CREDIT));
		when(accountService.findAccount(any())).thenReturn(account);
		when(transactionService.listTransactions(any(), anyLong(), anyLong(), anyInt())).thenReturn(accountTransactionss);
		RequestBuilder builder = MockMvcRequestBuilders.get("/api/transactions/accounts/1").params(params).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();

		JSONAssert.assertEquals("[{\"id\":null,\"accountId\":1,\"body\":\"body\",\"transactionType\":\"CREDIT\",\"transactionOn\":\"2018-06-30T20:39:03.000+0000\"}]",mvcResult.getResponse().getContentAsString(),false);
	}

	@Test
	public void test_list_transaction_failure() throws Exception {
		when(accountService.findAccount(any())).thenThrow(new TransactionException("Exception"));
		when(transactionService.debitAmountToAccount(any(), any())).thenReturn(account);
		RequestBuilder builder = MockMvcRequestBuilders.get("/api/transactions/accounts/1").accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();

		JSONAssert.assertEquals("[{\"message\":\"Exception\",\"code\":\"PRECONDITION_FAILED\"}]",mvcResult.getResponse().getContentAsString(),false);
	}


	private Account getAccount() {
		return Account.builder().id(1l).userId(getUser()).balance(new BigDecimal(100)).accountStatus(AccountStatus.ACTIVE).accountType(AccountType.SAVINGS).createdOn(new Date(1530391143000l)).build();
	}
	private User getUser() {
		return User.builder().id(2l).username("abc").password("qwerty").build();
	}

	private AccountTransactions getAccountTransactions(TransactionType transactionType) {
		return AccountTransactions.builder().accountId(account).body("body").transactionOn(new Date(1530391143000l)).transactionType(transactionType).build();
	}
}
