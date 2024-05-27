CREATE TABLE favourites_advertisements
(
    id               BIGSERIAL PRIMARY KEY,
    user_id          BIGINT,
    advertisement_id BIGINT,
    CONSTRAINT fk_favourite_advertisement FOREIGN KEY (advertisement_id) REFERENCES advertisements (id)
);