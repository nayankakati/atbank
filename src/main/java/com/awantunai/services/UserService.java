package com.awantunai.services;

import com.awantunai.entities.Account;
import com.awantunai.entities.User;
import com.awantunai.exceptions.UserException;
import com.awantunai.requests.UserRequest;


public interface UserService {
	User createUser(UserRequest user) throws UserException;
	Account loginUser(UserRequest user) throws UserException;
	User updateUser(UserRequest user) throws UserException;
	User deleteUser(UserRequest user) throws UserException;
	User findUser(Long userId) throws UserException;
}
