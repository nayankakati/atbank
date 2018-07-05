package com.awantunai.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.awantunai.entities.Account;
import com.awantunai.entities.AccountTransactions;
import com.awantunai.entities.User;


@Repository
public interface TransactionRepository extends JpaRepository<AccountTransactions, Long>, TransactionRepositoryCustom {
	@Transactional
	@Modifying
	@Query("DELETE FROM AccountTransactions a where a.accountId=:accountId")
	void deleteAccountTransactionByAccountId(@Param("accountId") Account accountId);
}
