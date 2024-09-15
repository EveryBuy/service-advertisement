ALTER TABLE advertisement_delivery_methods
DROP CONSTRAINT fk_advertisement_delivery_methods_advertisement_id,
    DROP CONSTRAINT fk_advertisement_delivery_methods_delivery_method;

ALTER TABLE advertisement_delivery_methods
DROP COLUMN advertisement_id,
    DROP COLUMN delivery_method;

DROP TABLE IF EXISTS advertisement_delivery_methods;

DROP TABLE IF EXISTS delivery_methods;
