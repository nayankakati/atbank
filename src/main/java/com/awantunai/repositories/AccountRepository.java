package com.awantunai.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.awantunai.entities.Account;
import com.awantunai.entities.User;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	@Query("SELECT a FROM Account a where a.userId=:userId")
	Account findAccountByUserId(@Param("userId") User userId);
}
