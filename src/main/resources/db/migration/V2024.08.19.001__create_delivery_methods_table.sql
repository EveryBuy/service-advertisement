CREATE TABLE advertisement_delivery
(
    id               BIGSERIAL PRIMARY KEY,
    advertisement_id BIGINT       NOT NULL,
    delivery_method  VARCHAR(255) NOT NULL,
    CONSTRAINT fk_advertisement
        FOREIGN KEY (advertisement_id)
            REFERENCES advertisements (id)
            ON DELETE CASCADE
);
