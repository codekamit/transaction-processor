CREATE SCHEMA IF NOT EXISTS orm;

CREATE SEQUENCE orm.upload_sequence
    START WITH 1
    INCREMENT BY 20
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE orm.earning_sequence
    START WITH 1
    INCREMENT BY 5000
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE orm.billing_sequence
    START WITH 1
    INCREMENT BY 1000
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE orm.settlement_upload (
    id BIGINT NOT NULL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    settlement_type VARCHAR(255) NOT NULL,
    file_size VARCHAR(255) NOT NULL,
    upload_status VARCHAR(255) NOT NULL
);

CREATE TABLE orm.earning (
    id BIGINT NOT NULL PRIMARY KEY,
    currency VARCHAR(255) NOT NULL,
    client_name VARCHAR(255) NOT NULL,
    fund VARCHAR(255) NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    settlement_id BIGINT,
    CONSTRAINT fk_earning_to_settlement_upload
        FOREIGN KEY (settlement_id)
        REFERENCES orm.settlement_upload (id)
);

CREATE TABLE orm.billing (
    id BIGINT NOT NULL PRIMARY KEY,
    received_amount DOUBLE PRECISION NOT NULL,
    currency VARCHAR(255) NOT NULL,
    broker_number VARCHAR(255) NOT NULL,
    broker_shortname VARCHAR(255) NOT NULL,
    settlement_id BIGINT,
    CONSTRAINT fk_billing_to_settlement_upload
        FOREIGN KEY (settlement_id)
        REFERENCES orm.settlement_upload (id)
);

CREATE INDEX idx_earning_settlement_id ON orm.earning (settlement_id);
CREATE INDEX idx_billing_settlement_id ON orm.billing (settlement_id);