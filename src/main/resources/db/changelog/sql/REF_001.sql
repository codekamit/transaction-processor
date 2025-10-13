CREATE SCHEMA IF NOT EXISTS orm;

CREATE TABLE orm.settlement_upload (
    id UUID NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    settlement_type VARCHAR(255) NOT NULL,
    file_size VARCHAR(255) NOT NULL,
    upload_status VARCHAR(255) NOT NULL,
    CONSTRAINT settlement_upload_pkey PRIMARY KEY (id)
);

CREATE TABLE orm.earning (
    id UUID NOT NULL,
    currency VARCHAR(255) NOT NULL,
    client_name VARCHAR(255) NOT NULL,
    fund VARCHAR(255) NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    settlement_id UUID,
    CONSTRAINT pk_earning PRIMARY KEY (id),
    CONSTRAINT fk_earning_settlement_upload
        FOREIGN KEY (settlement_id)
        REFERENCES orm.settlement_upload (id)
);

CREATE TABLE orm.billing (
    id UUID NOT NULL,
    received_amount DOUBLE PRECISION NOT NULL,
    currency VARCHAR(255) NOT NULL,
    broker_number VARCHAR(255) NOT NULL,
    broker_shortname VARCHAR(255) NOT NULL,
    settlement_id UUID,
    CONSTRAINT pk_billing PRIMARY KEY (id),
    CONSTRAINT fk_billing_settlement_upload
        FOREIGN KEY (settlement_id)
        REFERENCES orm.settlement_upload (id)
);

CREATE INDEX idx_earning_settlement_id ON orm.earning (settlement_id);
CREATE INDEX idx_billing_settlement_id ON orm.billing (settlement_id);