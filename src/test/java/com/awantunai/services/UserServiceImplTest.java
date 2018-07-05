package com.awantunai.services;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.awantunai.entities.Account;
import com.awantunai.entities.User;
import com.awantunai.enums.AccountStatus;
import com.awantunai.enums.AccountType;
import com.awantunai.exceptions.UserException;
import com.awantunai.repositories.AccountRepository;
import com.awantunai.repositories.UserRepository;
import com.awantunai.requests.UserRequest;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private AccountRepository accountRepository;

	@InjectMocks
	private UserServiceImpl userServiceImpl;
	private User user;
	private Account account;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		user = getUser();
		account = Account.builder().id(1l).userId(getUser()).balance(new BigDecimal(100)).accountStatus(AccountStatus.ACTIVE).accountType(AccountType.SAVINGS).createdOn(new Date(1530391143000l)).build();
	}

	@Test
	public void test_create_user_success() throws UserException{
		UserRequest userRequest = UserRequest.builder().username("def").password("qwed").build();
		when(userRepository.findByUserName(anyString())).thenReturn(null);
		when(userRepository.save(any())).thenReturn(user);
		User actualUser = userServiceImpl.createUser(userRequest);
		assertEquals(actualUser.getId(), user.getId());
		assertEquals(actualUser.getUsername(), user.getUsername());
		assertEquals(actualUser.getPassword(), user.getPassword());
	}

	@Test
	public void test_create_user_failure() throws UserException{
		UserRequest userRequest = UserRequest.builder().username("def").password("qwed").build();
		when(userRepository.findByUserName(anyString())).thenReturn(user);
		assertThatExceptionOfType(UserException.class)
			.isThrownBy(() -> userServiceImpl.createUser(userRequest))
			.is(new Condition<>(e -> Objects.nonNull(e), "The username " + userRequest.getUsername() + " is already exits, Please select a different username."));
	}

	@Test
	public void test_update_user_success() throws UserException{
		UserRequest userRequest = UserRequest.builder().username("def").password("qwd").build();
		when(userRepository.findByUserName(anyString())).thenReturn(user);
		when(userRepository.save(any())).thenReturn(user);
		User actualUser = userServiceImpl.updateUser(userRequest);
		assertEquals(actualUser.getId(), user.getId());
		assertEquals(actualUser.getUsername(), user.getUsername());
		assertEquals(actualUser.getPassword(), user.getPassword());
	}

	@Test
	public void test_update_user_failure() throws UserException{
		UserRequest userRequest = UserRequest.builder().username("def").password("qwed").build();
		when(userRepository.findByUserName(anyString())).thenReturn(null);
		assertThatExceptionOfType(UserException.class)
			.isThrownBy(() -> userServiceImpl.updateUser(userRequest))
			.is(new Condition<>(e -> Objects.nonNull(e), "The username " + userRequest.getUsername() + " does not exists."));
	}

	@Test
	public void test_delete_user_success() throws UserException{
		UserRequest userRequest = UserRequest.builder().username("def").password("qwd").build();
		when(userRepository.findByUserName(anyString())).thenReturn(user);
		doNothing().when(userRepository).delete(any());
		User actualUser = userServiceImpl.deleteUser(userRequest);
		assertEquals(actualUser.getId(), user.getId());
		assertEquals(actualUser.getUsername(), user.getUsername());
		assertEquals(actualUser.getPassword(), user.getPassword());
	}

	@Test
	public void test_delete_user_failure() throws UserException{
		UserRequest userRequest = UserRequest.builder().username("def").password("qwed").build();
		when(userRepository.findByUserName(anyString())).thenReturn(null);
		assertThatExceptionOfType(UserException.class)
			.isThrownBy(() -> userServiceImpl.deleteUser(userRequest))
			.is(new Condition<>(e -> Objects.nonNull(e), "The username " + userRequest.getUsername() + " does not exists."));
	}

	@Test
	public void test_find_user_success() throws UserException{
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		User actualUser = userServiceImpl.findUser(user.getId());
		assertEquals(actualUser.getId(), user.getId());
		assertEquals(actualUser.getUsername(), user.getUsername());
		assertEquals(actualUser.getPassword(), user.getPassword());
	}

	@Test
	public void test_find_user_failure() throws UserException{
		when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
		assertThatExceptionOfType(UserException.class)
			.isThrownBy(() -> userServiceImpl.findUser(99l))
			.is(new Condition<>(e -> Objects.nonNull(e), "The user id " + 99l + " does not exists."));
	}

	@Test
	public void test_login_user_success() throws UserException{
		UserRequest userRequest = UserRequest.builder().username("def").password("qwerty").build();
		when(userRepository.findByUserName(anyString())).thenReturn(user);
		when(accountRepository.findAccountByUserId(any())).thenReturn(account);
		Account actualAccount = userServiceImpl.loginUser(userRequest);
		assertEquals(actualAccount.getId(), account.getId());
	}

	@Test
	public void test_login_user_failure() throws UserException {
		UserRequest userRequest = UserRequest.builder().username("def").password("qwed").build();
		when(userRepository.findByUserName(anyString())).thenReturn(null);
		when(accountRepository.findAccountByUserId(any())).thenReturn(null);
		assertThatExceptionOfType(UserException.class)
			.isThrownBy(() -> userServiceImpl.loginUser(userRequest))
			.is(new Condition<>(e -> Objects.nonNull(e), "Invalid username or password."));
	}

	@Test
	public void test_login_user_account_failure() throws UserException {
		UserRequest userRequest = UserRequest.builder().username("def").password("qwerty").build();
		when(userRepository.findByUserName(anyString())).thenReturn(user);
		when(accountRepository.findAccountByUserId(any())).thenReturn(null);
		assertThatExceptionOfType(UserException.class)
			.isThrownBy(() -> userServiceImpl.loginUser(userRequest))
			.is(new Condition<>(e -> Objects.nonNull(e), "Please create an Account first."));
	}

	private User getUser() {
		return User.builder().id(2l).username("abc").password("qwerty").build();
	}
}
