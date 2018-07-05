package com.awantunai.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.awantunai.enums.TransactionType;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "account_transactions")
public class AccountTransactions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account accountId;

	@Column(name = "transaction_on")
	private Date transactionOn;

	@Column(name = "body", columnDefinition = "LONGTEXT", length = 65535)
	private String body;

	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;
}
