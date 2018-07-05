package com.awantunai.repositories;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Service;

import com.awantunai.entities.Account;
import com.awantunai.entities.AccountTransactions;

@Service
public class TransactionRepositoryImpl implements TransactionRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	public List<AccountTransactions> listTransactions(Account account, Long from, Long to, Integer size) throws RuntimeException {
		TypedQuery<AccountTransactions> query = getAccountTransactionsTypedQuery(account, from, to, size);
		List<AccountTransactions> scanDetails = query.getResultList();
		return scanDetails;
	}

	private TypedQuery<AccountTransactions> getAccountTransactionsTypedQuery(Account account, Long from, Long to, Integer size) throws RuntimeException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<AccountTransactions> query = cb.createQuery(AccountTransactions.class);
		Root<AccountTransactions> root = query.from(AccountTransactions.class);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(root.get("accountId"), account));

		if(from != null)
			predicates.add(cb.greaterThanOrEqualTo(root.get("transactionOn"), Date.from(Instant.ofEpochMilli(from))));

		if(to != null)
			predicates.add(cb.lessThanOrEqualTo(root.get("transactionOn"), Date.from(Instant.ofEpochMilli(to))));


		CriteriaQuery<AccountTransactions> criteriaQuery = query.select(root).where(predicates.toArray(new Predicate[]{}));

		criteriaQuery.orderBy(cb.desc(root.get("transactionOn")));
		TypedQuery<AccountTransactions> accountTranTypedQuery = entityManager.createQuery(criteriaQuery);
		accountTranTypedQuery.setMaxResults((size != null) ? size : 10);
		return accountTranTypedQuery;
	}
}
