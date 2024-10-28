ALTER TABLE advertisements
DROP CONSTRAINT advertisements_product_type_check;

ALTER TABLE advertisements
    ADD CONSTRAINT advertisements_product_type_check
        CHECK (product_type IN ('NEW', 'USED', 'OTHER'));
