CREATE SCHEMA IF NOT EXISTS orm;

CREATE TABLE IF NOT EXISTS orm.settlement_upload (
    id UUID NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_size VARCHAR(255) NOT NULL,
    settlement_type VARCHAR(255) NOT NULL, -- Assuming ENUM as VARCHAR
    upload_status VARCHAR(255) NOT NULL,   -- Assuming ENUM as VARCHAR
    CONSTRAINT pk_settlement_upload PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS orm.net_earning (
    id VARCHAR(16) NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    client_name VARCHAR(255) NOT NULL,
    payment_fund VARCHAR(255) NOT NULL,
    settlement_link_status VARCHAR(255) NOT NULL, -- Assuming ENUM as VARCHAR
    settlement_level VARCHAR(255) NOT NULL,       -- Assuming ENUM as VARCHAR
    settlement_type VARCHAR(255) NOT NULL,      -- Assuming ENUM as VARCHAR
    state VARCHAR(255),                         -- Assuming ENUM as VARCHAR
    currency VARCHAR(255) NOT NULL,
    netting BOOLEAN NOT NULL,
    settlement_id UUID,
    CONSTRAINT pk_net_earning PRIMARY KEY (id),
    CONSTRAINT fk_net_earning_on_settlement FOREIGN KEY (settlement_id) REFERENCES orm.settlement_upload (id)
);

CREATE TABLE IF NOT EXISTS orm.earning (
    id VARCHAR(16) NOT NULL,
    currency VARCHAR(255) NOT NULL,           -- Assuming ENUM as VARCHAR
    client_name VARCHAR(255) NOT NULL,
    fund VARCHAR(255) NOT NULL,
    state VARCHAR(255),                       -- Assuming ENUM as VARCHAR
    amount DOUBLE PRECISION NOT NULL,
    settlement_id UUID,
    net_earning_id VARCHAR(16),
    CONSTRAINT pk_earning PRIMARY KEY (id),
    CONSTRAINT fk_earning_on_settlement FOREIGN KEY (settlement_id) REFERENCES orm.settlement_upload (id),
    CONSTRAINT fk_earning_on_net_earning FOREIGN KEY (net_earning_id) REFERENCES orm.net_earning (id)
);

CREATE TABLE IF NOT EXISTS orm.net_earning_last_linked (
    net_earning_id VARCHAR(16) NOT NULL,
    earning_id VARCHAR(255),
    CONSTRAINT fk_net_earning_last_linked_on_net_earning FOREIGN KEY (net_earning_id) REFERENCES orm.net_earning (id)
);

CREATE INDEX IF NOT EXISTS idx_net_earning_settlement_id ON orm.net_earning(settlement_id);
CREATE INDEX IF NOT EXISTS idx_earning_settlement_id ON orm.earning(settlement_id);
CREATE INDEX IF NOT EXISTS idx_earning_net_earning_id ON orm.earning(net_earning_id);
CREATE INDEX IF NOT EXISTS idx_net_earning_last_linked_id ON orm.net_earning_last_linked(net_earning_id);

CREATE TABLE IF NOT EXISTS orm.net_billing (
    id VARCHAR(16) NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    broker_number VARCHAR(255) NOT NULL,
    currency VARCHAR(255) NOT NULL,
    payment_broker_number VARCHAR(255) NOT NULL,
    settlement_level VARCHAR(255) NOT NULL,
    settlement_type VARCHAR(255) NOT NULL,
    settlement_id UUID,
    CONSTRAINT pk_net_billing PRIMARY KEY (id),
    CONSTRAINT fk_net_billing_settlement_upload FOREIGN KEY (settlement_id)
        REFERENCES orm.settlement_upload (id)
);

CREATE TABLE IF NOT EXISTS orm.billing (
    id VARCHAR(16) NOT NULL,
    broker_number VARCHAR(255) NOT NULL,
    broker_shortname VARCHAR(255) NOT NULL,
    currency VARCHAR(255) NOT NULL,
    received_amount DOUBLE PRECISION NOT NULL,
    net_billing_id VARCHAR(16), -- This FK was missing from your entities
    settlement_id UUID,
    CONSTRAINT pk_billing PRIMARY KEY (id),
    CONSTRAINT fk_billing_net_billing FOREIGN KEY (net_billing_id)
        REFERENCES orm.net_billing (id),
    CONSTRAINT fk_billing_settlement_upload FOREIGN KEY (settlement_id)
        REFERENCES orm.settlement_upload (id)
);