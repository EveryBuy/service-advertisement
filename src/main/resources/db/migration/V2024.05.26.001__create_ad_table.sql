CREATE TABLE advertisements
(
    id              BIGSERIAL PRIMARY KEY,
    price           VARCHAR(55)   NOT NULL,
    title           VARCHAR(255)  NOT NULL,
    description     VARCHAR(1000) NOT NULL,
    product_type    VARCHAR(10)   NOT NULL CHECK (product_type IN ('NEW', 'USED')),
    subcategory_id     INT           NOT NULL,
    creation_date   TIMESTAMP     NOT NULL,
    is_enabled      BOOLEAN DEFAULT TRUE,
    user_id         BIGINT        NOT NULL,
    city_id         INT           NOT NULL,
    delivery_method VARCHAR(25)   NOT NULL CHECK (product_type IN ('NOVA_POST', 'UKR_POST', 'MEEST_EXPRESS')),
    FOREIGN KEY (subcategory_id) REFERENCES subcategories (id),
    FOREIGN KEY (city_id) REFERENCES cities (id)
);

CREATE TABLE advertisement_photos
(
    id               SERIAL PRIMARY KEY,
    advertisement_id BIGINT       NOT NULL,
    photo_url        VARCHAR(500) NOT NULL,
    creation_date    TIMESTAMP    NOT NULL,
    FOREIGN KEY (advertisement_id) REFERENCES advertisements (id)
);