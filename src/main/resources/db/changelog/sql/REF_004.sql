
CREATE TABLE orm.net_earning_last_linked (
    net_earning_id UUID NOT NULL,
    earning_id UUID NOT NULL,
    CONSTRAINT fk_ne_last_link FOREIGN KEY (net_earning_id)
        REFERENCES orm.net_earning (id)
);