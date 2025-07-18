CREATE TABLE wallets (
     id              VARCHAR(36) NOT NULL PRIMARY KEY,  -- matches `private String id;`
     deposit_address VARCHAR(50) NOT NULL,
     crypto_currency VARCHAR(25) NOT NULL,
     network         VARCHAR(20) NOT NULL ,
     is_active       BOOLEAN DEFAULT TRUE
);

CREATE TABLE transactions (
      id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
      phone_number       VARCHAR(15) NOT NULL,
      network            VARCHAR(20) NOT NULL,
      service_id         VARCHAR(30) NOT NULL,                      -- new
      billers_code       VARCHAR(30) NOT NULL,                      -- new
      data_plan_code     VARCHAR(25) NOT NULL,
      amount_naira       DECIMAL(10, 2) NOT NULL,
      amount_crypto      DECIMAL(20, 8) NOT NULL,
      crypto_currency    VARCHAR(25) NOT NULL,
      wallet_id          VARCHAR(36) NOT NULL,
      transaction_status VARCHAR(20) NOT NULL,
      delivery_status    VARCHAR(20) NOT NULL,
      request_id         VARCHAR(50) NOT NULL,                      -- from VTPass
      transaction_hash   VARCHAR(100),                     -- from Quidax
      transaction_id     VARCHAR(50),                      -- from Quidax
      created_at         DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
      expires_at         DATETIME NOT NULL,                         -- new
      CONSTRAINT transactions_wallets_id_fk
          FOREIGN KEY (wallet_id) REFERENCES wallets(id)
);





