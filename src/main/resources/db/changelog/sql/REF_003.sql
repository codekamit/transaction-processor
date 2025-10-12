CREATE SCHEMA IF NOT EXISTS orm;

CREATE SEQUENCE orm.net_billing_sequence
    START WITH 1
    INCREMENT BY 1000
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE orm.net_earning_sequence
    START WITH 1
    INCREMENT BY 1000
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE orm.net_billing (
    id BIGINT NOT NULL PRIMARY KEY,
    broker_number VARCHAR(255) NOT NULL,
    payment_broker_number VARCHAR(255) NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    currency VARCHAR(255) NOT NULL,
    settlement_level VARCHAR(255) NOT NULL,
    settlement_type VARCHAR(255) NOT NULL,
    settlement_id BIGINT,
    CONSTRAINT fk_net_billing_to_settlement_upload
        FOREIGN KEY (settlement_id)
        REFERENCES orm.settlement_upload (id)
);

CREATE TABLE orm.net_earning (
    id BIGINT NOT NULL PRIMARY KEY,
    amount DOUBLE PRECISION NOT NULL,
    client_name VARCHAR(255) NOT NULL,
    fund VARCHAR(255) NOT NULL,
    settlement_level VARCHAR(255) NOT NULL,
    settlement_type VARCHAR(255) NOT NULL,
    currency VARCHAR(255) NOT NULL,
    settlement_id BIGINT,
    CONSTRAINT fk_net_earning_to_settlement_upload
        FOREIGN KEY (settlement_id)
        REFERENCES orm.settlement_upload (id)
);

ALTER TABLE orm.billing
ADD COLUMN net_billing_id BIGINT;

ALTER TABLE orm.billing
ADD CONSTRAINT fk_billing_to_net_billing
    FOREIGN KEY (net_billing_id)
    REFERENCES orm.net_billing (id);

ALTER TABLE orm.earning
ADD COLUMN net_earning_id BIGINT;

ALTER TABLE orm.earning
ADD CONSTRAINT fk_earning_to_net_earning
    FOREIGN KEY (net_earning_id)
    REFERENCES orm.net_earning (id);