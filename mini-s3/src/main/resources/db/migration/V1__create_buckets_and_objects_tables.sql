CREATE TABLE buckets (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL UNIQUE,
                         created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE stored_objects (
                                id BIGSERIAL PRIMARY KEY,
                                key VARCHAR(1024) NOT NULL,
                                bucket_id BIGINT NOT NULL REFERENCES buckets(id),
                                size BIGINT NOT NULL,
                                content_type VARCHAR(255),
                                storage_path VARCHAR(512) NOT NULL,
                                last_modified TIMESTAMP NOT NULL DEFAULT now(),
                                CONSTRAINT uq_bucket_key UNIQUE (bucket_id, key)
);