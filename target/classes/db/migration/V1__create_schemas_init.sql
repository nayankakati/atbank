CREATE TABLE users (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  username varchar(255)  NOT NULL ,
  password varchar(255)  NOT NULL ,
  PRIMARY KEY (id),
  INDEX users_ind (id)
) AUTO_INCREMENT=1;

CREATE TABLE accounts (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20) NOT NULL,
  account_type varchar(15) NOT NULL,
  balance numeric NOT NULL default 0.0,
  created_on datetime NOT NULL,
  status varchar(15) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_users FOREIGN KEY (user_id)
  REFERENCES users(id),
  INDEX ac_ind (id),
  INDEX ac_user_id (user_id)
);

CREATE TABLE account_transactions (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  account_id bigint(20) NOT NULL,
  body LONGTEXT,
  type varchar(15) NOT NULL,
  transaction_on datetime NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_accounts FOREIGN KEY (account_id)
  REFERENCES accounts(id),
  INDEX tran_ind (id),
  INDEX tran_ac_id (account_id)
);
