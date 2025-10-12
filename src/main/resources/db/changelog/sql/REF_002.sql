CREATE SCHEMA IF NOT EXISTS orm;

CREATE SEQUENCE orm.client_preference_sequence
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE orm.fund_group_sequence
    START WITH 1
    INCREMENT BY 100
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE orm.client_preference (
    id BIGINT NOT NULL PRIMARY KEY,
    client_name VARCHAR(255) NOT NULL,
    settlement_level VARCHAR(255) NOT NULL,
    currency VARCHAR(255) NOT NULL,
    netting BOOLEAN NOT NULL,
    status VARCHAR(255) NOT NULL,
    CONSTRAINT uc_client_currency UNIQUE (client_name, currency)
);

CREATE TABLE orm.fund_group (
    id BIGINT NOT NULL PRIMARY KEY,
    fund VARCHAR(255) NOT NULL,
    payment_fund VARCHAR(255) NOT NULL,
    client_preference_id BIGINT,
    CONSTRAINT fk_fund_group_to_client_preference
        FOREIGN KEY (client_preference_id)
        REFERENCES orm.client_preference (id)
);