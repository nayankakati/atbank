package com.awantunai.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.awantunai.entities.Account;
import com.awantunai.entities.User;
import com.awantunai.exceptions.UserException;
import com.awantunai.repositories.AccountRepository;
import com.awantunai.repositories.UserRepository;
import com.awantunai.requests.UserRequest;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AccountRepository accountRepository;

	public User createUser(UserRequest user) throws UserException {
		User existingUser  = userRepository.findByUserName(user.getUsername());

		if(existingUser != null)
			throw new UserException("The username " + user.getUsername() + " is already exits, Please select a different username.");

		return userRepository.save(getUser(user));
	}

	public Account loginUser(UserRequest user) throws UserException {
		User existingUser  = userRepository.findByUserName(user.getUsername());

		if(existingUser == null || !(existingUser.getPassword().equals(user.getPassword())))
			throw new UserException("Invalid username or password.");

		Account account = accountRepository.findAccountByUserId(existingUser);
		if(account == null)
			throw new UserException("Please create an Account first.");
		return account;
	}

	public User updateUser(UserRequest user) throws UserException {
		User userToUpdate  = userRepository.findByUserName(user.getUsername());

		if(userToUpdate == null)
			throw new UserException("The username " + user.getUsername() + " does not exists.");

		userToUpdate.setPassword(user.getPassword().isEmpty() ? userToUpdate.getPassword() : user.getPassword());
		userRepository.save(userToUpdate);
		return userToUpdate;
	}

	public User deleteUser(UserRequest user) throws UserException {
		User userToDelete  = userRepository.findByUserName(user.getUsername());

		if(userToDelete == null)
			throw new UserException("The username " + user.getUsername() + " does not exists.");

		userRepository.delete(userToDelete);
		return userToDelete;
	}

	public User findUser(Long userId) throws UserException {
		Optional<User> user = userRepository.findById(userId);

		if(user.orElse(null) == null)
			throw new UserException("The user id " + userId + " does not exists.");

		return user.get();
	}

	private User getUser(UserRequest userRequest) {
		return User.builder().username(userRequest.getUsername()).password(userRequest.getPassword()).build();
	}
}
