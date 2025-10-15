CREATE SCHEMA IF NOT EXISTS orm;

CREATE TABLE IF NOT EXISTS orm.settlement_upload (
    id UUID NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_size VARCHAR(255) NOT NULL,
    settlement_type VARCHAR(255) NOT NULL,
    upload_status VARCHAR(255) NOT NULL,
    CONSTRAINT settlement_upload_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS orm.net_earning (
    id UUID NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    client_name VARCHAR(255) NOT NULL,
    currency VARCHAR(255) NOT NULL,
    payment_fund VARCHAR(255) NOT NULL,
    netting BOOLEAN NOT NULL,
    settlement_level VARCHAR(255) NOT NULL,
    settlement_type VARCHAR(255) NOT NULL,
    state VARCHAR(255),
    settlement_id UUID,
    CONSTRAINT net_earning_pkey PRIMARY KEY (id),
    CONSTRAINT fk_netearning_settlement_upload FOREIGN KEY (settlement_id)
        REFERENCES orm.settlement_upload (id)
);

CREATE TABLE IF NOT EXISTS orm.net_billing (
    id UUID NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    broker_number VARCHAR(255) NOT NULL,
    currency VARCHAR(255) NOT NULL,
    payment_broker_number VARCHAR(255) NOT NULL,
    settlement_level VARCHAR(255) NOT NULL,
    settlement_type VARCHAR(255) NOT NULL,
    settlement_id UUID,
    CONSTRAINT net_billing_pkey PRIMARY KEY (id),
    CONSTRAINT fk_netbilling_settlement_upload FOREIGN KEY (settlement_id)
        REFERENCES orm.settlement_upload (id)
);

CREATE TABLE IF NOT EXISTS orm.earning (
    id UUID NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    client_name VARCHAR(255) NOT NULL,
    currency VARCHAR(255) NOT NULL,
    fund VARCHAR(255) NOT NULL,
    state VARCHAR(255),
    net_earning_id UUID,
    settlement_id UUID,
    CONSTRAINT earning_pkey PRIMARY KEY (id),
    CONSTRAINT fk_earning_net_earning FOREIGN KEY (net_earning_id)
        REFERENCES orm.net_earning (id),
    CONSTRAINT fk_earning_settlement_upload FOREIGN KEY (settlement_id)
        REFERENCES orm.settlement_upload (id)
);

CREATE TABLE IF NOT EXISTS orm.billing (
    id UUID NOT NULL,
    broker_number VARCHAR(255) NOT NULL,
    broker_shortname VARCHAR(255) NOT NULL,
    currency VARCHAR(255) NOT NULL,
    received_amount DOUBLE PRECISION NOT NULL,
    net_earning_id UUID,
    net_billing_id UUID, -- This FK was missing from your entities
    settlement_id UUID,
    CONSTRAINT billing_pkey PRIMARY KEY (id),
    CONSTRAINT fk_billing_net_earning FOREIGN KEY (net_earning_id)
        REFERENCES orm.net_earning (id),
    CONSTRAINT fk_billing_net_billing FOREIGN KEY (net_billing_id)
        REFERENCES orm.net_billing (id),
    CONSTRAINT fk_billing_settlement_upload FOREIGN KEY (settlement_id)
        REFERENCES orm.settlement_upload (id)
);