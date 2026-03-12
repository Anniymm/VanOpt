CREATE TABLE optimization_requests (
    id UUID NOT NULL,
    max_volume INTEGER NOT NULL,
    total_volume  INTEGER NOT NULL,
    total_revenue BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT pk_optimization_requests PRIMARY KEY (id)
);

CREATE INDEX idx_optimization_requests_created_at
    ON optimization_requests (created_at DESC);