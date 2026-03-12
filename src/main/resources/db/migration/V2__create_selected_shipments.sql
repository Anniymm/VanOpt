CREATE TABLE selected_shipments (
    id UUID NOT NULL,
    request_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    volume INTEGER NOT NULL,
    revenue    BIGINT       NOT NULL,
    CONSTRAINT pk_selected_shipments PRIMARY KEY (id),
    CONSTRAINT fk_selected_shipments_request
        FOREIGN KEY (request_id)
            REFERENCES optimization_requests (id)
            ON DELETE CASCADE
);

CREATE INDEX idx_selected_shipments_request_id
    ON selected_shipments (request_id);