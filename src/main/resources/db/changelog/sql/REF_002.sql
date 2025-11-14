CREATE SCHEMA IF NOT EXISTS orm;

CREATE TABLE orm.client_preference (
    id UUID NOT NULL,
    client_name VARCHAR(255) NOT NULL,
    settlement_level VARCHAR(255) NOT NULL,
    currency VARCHAR(255) NOT NULL,
    netting BOOLEAN NOT NULL,
    status VARCHAR(255) NOT NULL,
    version BIGINT DEFAULT 0 NOT NULL,
    CONSTRAINT pk_client_preference PRIMARY KEY (id),
    CONSTRAINT uc_client_currency UNIQUE (client_name, currency)
);

CREATE TABLE orm.fund_group (
    id UUID NOT NULL,
    fund VARCHAR(255) NOT NULL,
    payment_fund VARCHAR(255) NOT NULL,
    client_preference_id UUID,
    CONSTRAINT pk_fund_group PRIMARY KEY (id),
    CONSTRAINT fk_fund_group_client_preference
        FOREIGN KEY (client_preference_id)
        REFERENCES orm.client_preference (id)
);