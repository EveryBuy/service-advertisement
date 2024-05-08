ALTER TABLE advertisements
DROP COLUMN delivery_method;

CREATE TABLE delivery_methods
(
name VARCHAR(25) PRIMARY KEY);

INSERT INTO delivery_methods (name) VALUES
('NOVA_POST'),
('UKR_POST'),
('MEEST_EXPRESS');

CREATE TABLE advertisement_delivery_methods
(
    advertisement_id BIGINT NOT NULL,
    delivery_method  VARCHAR(25) NOT NULL,
    CONSTRAINT fk_advertisement_delivery_methods_advertisement_id FOREIGN KEY (advertisement_id) REFERENCES advertisements (id) ON DELETE CASCADE,
    CONSTRAINT fk_advertisement_delivery_methods_delivery_method FOREIGN KEY (delivery_method) REFERENCES delivery_methods (name) ON DELETE RESTRICT,
    PRIMARY KEY (advertisement_id, delivery_method)
);